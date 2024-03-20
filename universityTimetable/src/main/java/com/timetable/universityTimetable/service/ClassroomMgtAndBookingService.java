package com.timetable.universityTimetable.service;

import java.util.List;


import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.ClassroomMgtAndBooking;

import jakarta.validation.ConstraintViolationException;

public interface ClassroomMgtAndBookingService {
	public void createClassroom(ClassroomMgtAndBooking classRoom)throws ConstraintViolationException, UniTimetableCollectionException;
	public List<ClassroomMgtAndBooking> getAllClassRooms();
	
	public ClassroomMgtAndBooking getClassroom(String classroomCode) throws UniTimetableCollectionException;
	
	public void updateClassroom(String classroomCode, ClassroomMgtAndBooking course) throws UniTimetableCollectionException;
	
	public void deleteByClassroomCode(String classroomCode) throws UniTimetableCollectionException;

}
