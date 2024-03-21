package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.repository.ClassRoomRepository;


import jakarta.validation.ConstraintViolationException;

@Service
public class ClassroomServiceImpl implements ClassroomService {
	
	@Autowired
	public ClassRoomRepository classRoomRepo;
	
	@Override
	public List<Classroom> getAllClassRooms() {
		List<Classroom> classrooms= classRoomRepo.findAll();
		if(classrooms.size()>0) {
			return classrooms;
		}else {
			return new ArrayList<Classroom>();
		}
	}

	@Override
	public Classroom getClassroom(String classroomCode) throws UniTimetableCollectionException {
		Optional<Classroom> classroomOpt = classRoomRepo.findByClassroomCode(classroomCode);
		if(!classroomOpt.isPresent()) {
			throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(classroomCode));
		}else {
			return classroomOpt.get();
		}
	}

	@Override
	public void createClassroom(Classroom classRoom)throws ConstraintViolationException, UniTimetableCollectionException {
		Optional<Classroom> classRoomOptional = classRoomRepo.findByClassroomCode(classRoom.getClassroomCode());
        if (classRoomOptional.isPresent()) {
            throw new UniTimetableCollectionException(UniTimetableCollectionException.ClassRoomAlreadyExist());
        } else {
            classRoom.setCreatedAt(new Date(System.currentTimeMillis()));
            classRoomRepo.save(classRoom);
        }
		
	}

	@Override
	public void updateClassroom(String classId, Classroom classRoom) throws UniTimetableCollectionException{
	Optional<Classroom> classOptId = classRoomRepo.findById(classId);
	Optional<Classroom> classOptCode = classRoomRepo.findByClassroomCode(classRoom.getClassroomCode());
	if (classOptId.isPresent()) {
    	if(classOptCode.isPresent()&& !classOptCode.get().getCid().equals(classId)) {
    		throw new UniTimetableCollectionException(UniTimetableCollectionException.ClassRoomAlreadyExist());
    	}
    	
    	Classroom classRoomUpdate= classOptId.get();
        
    	classRoomUpdate.setClassroomCode(classRoom.getClassroomCode());
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
		Optional<Classroom> classRoomOpt = classRoomRepo.findByClassroomCode(classroomCode);
		
		if(!classRoomOpt.isPresent()) {
			throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(classroomCode));
		}else {
			classRoomRepo.deleteByClassroomCode(classroomCode);
		}

		
	}



}
