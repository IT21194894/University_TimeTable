package com.timetable.universityTimetable.service;

import java.util.List;
import java.util.Map;

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.Timetable;

import jakarta.validation.ConstraintViolationException;

public interface TimeTableService {
	 public void createTimeTable(Timetable timeTable)throws ConstraintViolationException, TimeTableCollectionException;
	
	 public List<Map<String, Object>> getTimetableswithBooking();
	 
	//public Timetable getTimetable(String ttid) throws TimeTableCollectionException;
	 public Map<String, Object> getTimetableWithBookingDetails(String ttid) throws TimeTableCollectionException;
	
	 public void updateTimetable(String ttid, Timetable timetable) throws TimeTableCollectionException;
	
	 public void deleteByTtid(String ttid) throws TimeTableCollectionException;
	
	 public List<Map<String, Object>> getFacultyTimetables(String facultyUserName);
	 
	 public List<Map<String, Object>>  getStudentTimetables(String studentId);

}
