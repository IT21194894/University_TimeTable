package com.timetable.universityTimetable.service;

import java.util.List;


import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Classroom;

import jakarta.validation.ConstraintViolationException;

public interface ClassroomService {
	public void createClassroom(Classroom classRoom)throws ConstraintViolationException, UniTimetableCollectionException;
	public List<Classroom> getAllClassRooms();
	
	public Classroom getClassroom(String classroomCode) throws UniTimetableCollectionException;
	
	public void updateClassroom(String classroomCode, Classroom course) throws UniTimetableCollectionException;
	
	public void deleteByClassroomCode(String classroomCode) throws UniTimetableCollectionException;

}
