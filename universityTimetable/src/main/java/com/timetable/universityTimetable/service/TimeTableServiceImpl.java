package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Timetable;
import com.timetable.universityTimetable.repository.ClassRoomRepository;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.TimeTableRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class TimeTableServiceImpl implements TimeTableService {
	
	@Autowired
	public TimeTableRepository timeTableRepo;
	@Autowired
    private CourseRepository courseRepo;
    @Autowired
    private ClassRoomRepository classroomRepo;

	@Override
	public void createTimeTable(Timetable timeTable) throws ConstraintViolationException, TimeTableCollectionException {
		 Optional<Timetable> existingTimetable = timeTableRepo.findById(timeTable.getTtid());
	        Optional<Course> existingCourse = courseRepo.findByCourseCode(timeTable.getCourseCode());
	        Optional<Classroom> existingRoom = classroomRepo.findByClassroomCode(timeTable.getClassCode());
	        
	        if (existingTimetable.isPresent()) {
	            throw new TimeTableCollectionException(TimeTableCollectionException.TimeTableAlreadyExist());
	        } else {
	            if (existingCourse.isPresent()) {
	            	
	            	if (existingRoom.isPresent()) {
	            		timeTableRepo.save(timeTable);
	            	}else {
	            		throw new TimeTableCollectionException("Room not found" );
	            	}
	            } else {
	            
	                throw new TimeTableCollectionException("Course not found with code: " + timeTable.getCourseCode());
	            }
	        }
	    }


	@Override
	public void updateTimetable(String ttid, Timetable timetable) throws TimeTableCollectionException{
		Optional<Timetable> timeTablesOptId = timeTableRepo.findById(ttid);
        Optional<Course> existingCourse = courseRepo.findByCourseCode(timetable.getCourseCode());

        if (timeTablesOptId.isPresent()) {
        	
        	Timetable timetableUpdate= timeTablesOptId.get();
        	if (existingCourse.isPresent()) {
        	timetableUpdate.setStartTime(timetable.getStartTime());
        	timetableUpdate.setEndTime(timetable.getEndTime());
        	timetableUpdate.setFacultyCode(timetable.getFacultyCode());
        	timetableUpdate.setCourseCode(timetable.getCourseCode());
        	timetableUpdate.setClassCode(timetable.getClassCode());
//        	timetableUpdate.setPassword(timetable.get());
        	timetableUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
        	timeTableRepo.save(timetableUpdate);
        	 }else {
                 
                 throw new TimeTableCollectionException("Course not found with code: " + timetable.getCourseCode());
             }
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
