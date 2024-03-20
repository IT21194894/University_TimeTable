package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.exception.UserCollectionException;
import com.timetable.universityTimetable.modelclass.ClassroomMgtAndBooking;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.ClassRoomMgtAndBooikingRepository;


import jakarta.validation.ConstraintViolationException;

@Service
public class ClassroomMgtAndBookingServiceImpl implements ClassroomMgtAndBookingService {
	
	@Autowired
	public ClassRoomMgtAndBooikingRepository classRoomRepo;
	
	@Override
	public List<ClassroomMgtAndBooking> getAllClassRooms() {
		List<ClassroomMgtAndBooking> classrooms= classRoomRepo.findAll();
		if(classrooms.size()>0) {
			return classrooms;
		}else {
			return new ArrayList<ClassroomMgtAndBooking>();
		}
	}

	@Override
	public ClassroomMgtAndBooking getClassroom(String classroomCode) throws UniTimetableCollectionException {
		Optional<ClassroomMgtAndBooking> classroomOpt = classRoomRepo.findByClassroomCode(classroomCode);
		if(!classroomOpt.isPresent()) {
			throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(classroomCode));
		}else {
			return classroomOpt.get();
		}
	}

	@Override
	public void createClassroom(ClassroomMgtAndBooking classRoom)throws ConstraintViolationException, UniTimetableCollectionException {
		Optional<ClassroomMgtAndBooking> classRoomOptional = classRoomRepo.findByClassroomCode(classRoom.getClassroomCode());
        if (classRoomOptional.isPresent()) {
            throw new UniTimetableCollectionException(UniTimetableCollectionException.ClassRoomAlreadyExist());
        } else {
            classRoom.setCreatedAt(new Date(System.currentTimeMillis()));
            classRoomRepo.save(classRoom);
        }
		
	}

	@Override
	public void updateClassroom(String classId, ClassroomMgtAndBooking classRoom) throws UniTimetableCollectionException{
	Optional<ClassroomMgtAndBooking> classOptId = classRoomRepo.findById(classId);
	Optional<ClassroomMgtAndBooking> classOptCode = classRoomRepo.findByClassroomCode(classRoom.getClassroomCode());
	if (classOptId.isPresent()) {
    	if(classOptCode.isPresent()&& !classOptCode.get().getCid().equals(classId)) {
    		throw new UniTimetableCollectionException(UniTimetableCollectionException.ClassRoomAlreadyExist());
    	}
    	
    	ClassroomMgtAndBooking classRoomUpdate= classOptId.get();
        
    	classRoomUpdate.setClassroomCode(classRoom.getClassroomCode());
    	classRoomUpdate.setClassroomName(classRoom.getClassroomName());
    	classRoomUpdate.setCapacity(classRoom.getCapacity());
    	classRoomUpdate.setProjectorAvailable(classRoom.isProjectorAvailable());
    	classRoomUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
    	classRoomRepo.save(classRoomUpdate);
    } else {
    	throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(classId));
        
    }  
		
	}

	@Override
	public void deleteByClassroomCode(String classroomCode) throws UniTimetableCollectionException {
		Optional<ClassroomMgtAndBooking> classRoomOpt = classRoomRepo.findByClassroomCode(classroomCode);
		
		if(!classRoomOpt.isPresent()) {
			throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(classroomCode));
		}else {
			classRoomRepo.deleteByClassroomCode(classroomCode);
		}

		
	}



}
