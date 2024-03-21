package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Timetable;
import com.timetable.universityTimetable.repository.TimeTableRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class TimeTableServiceImpl implements TimeTableService {
	
	@Autowired
	public TimeTableRepository timeTableRepo;

	@Override
	public void createTimeTable(Timetable timeTable) throws ConstraintViolationException, TimeTableCollectionException {
		Optional<Timetable> timeTableOptional = timeTableRepo.findById(timeTable.getTtid());
        if (timeTableOptional.isPresent()) {
            throw new TimeTableCollectionException(TimeTableCollectionException.TimeTableAlreadyExist());
        } else {
        	timeTable.setCreatedAt(new Date(System.currentTimeMillis()));
        	timeTableRepo.save(timeTable);
        }
	}

	@Override
	public List<Timetable> getAllTimetables() {
		List<Timetable> timeTables= timeTableRepo.findAll();
		if(timeTables.size()>0) {
			return timeTables;
		}else {
			return new ArrayList<Timetable>();
		}
	}

	@Override
	public void updateTimetable(String ttid, Timetable timetable) throws TimeTableCollectionException{
		Optional<Timetable> timeTablesOptId = timeTableRepo.findById(ttid);
        if (timeTablesOptId.isPresent()) {
        	
        	Timetable timetableUpdate= timeTablesOptId.get();
        	timetableUpdate.setStartTime(timetable.getStartTime());
        	timetableUpdate.setEndTime(timetable.getEndTime());
        	timetableUpdate.setFacultyCode(timetable.getFacultyCode());
        	timetableUpdate.setCourseCode(timetable.getCourseCode());
        	timetableUpdate.setClassCode(timetable.getClassCode());
//        	timetableUpdate.setPassword(timetable.get());
        	timetableUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
        	timeTableRepo.save(timetableUpdate);
        } else {
        	throw new TimeTableCollectionException(TimeTableCollectionException.NotFoundException(ttid));
            
            
        }
	}

	@Override
	public void deleteByTtid(String ttid) throws TimeTableCollectionException {
		Optional<Timetable> timetableOpt = timeTableRepo.findById(ttid);
		
		if(!timetableOpt.isPresent()) {
			throw new TimeTableCollectionException(TimeTableCollectionException.NotFoundException(ttid));
		}else {
			timeTableRepo.deleteById(ttid);
		}

	}

	@Override
	public Timetable getTimetable(String ttid) throws TimeTableCollectionException {
		Optional<Timetable> timeOpt = timeTableRepo.findById(ttid);
		if(!timeOpt.isPresent()) {
			throw new TimeTableCollectionException(TimeTableCollectionException.NotFoundException(ttid));
		}else {
			return timeOpt.get();
		}
	}
	
	

}
