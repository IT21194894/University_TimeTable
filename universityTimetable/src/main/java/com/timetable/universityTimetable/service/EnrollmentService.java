package com.timetable.universityTimetable.service;

import java.util.List;

import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Enrollment;

import jakarta.validation.ConstraintViolationException;

public interface EnrollmentService {
public void createEnrollment(Enrollment enrollment)throws ConstraintViolationException,UniTimetableCollectionException;
	
	public List<Enrollment> getAllEnrollments();
	
	public Enrollment getEnrollment(String enrollid) throws UniTimetableCollectionException;
	
//	public void updateEnrollment(String enrollid, Enrollment enrollment) throws UniTimetableCollectionException;
	
	public void deleteEnrollment(String enrollid) throws UniTimetableCollectionException;
}
