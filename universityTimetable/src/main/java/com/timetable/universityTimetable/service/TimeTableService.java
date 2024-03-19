package com.timetable.universityTimetable.service;

import java.util.List;

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Timetable;

import jakarta.validation.ConstraintViolationException;

public interface TimeTableService {
	public void createTimeTable(Timetable timeTable)throws ConstraintViolationException, TimeTableCollectionException;
	public List<Timetable> getAllTimetables();
	
	//public Timetable getCourse(String courseCode) throws TimeLimitExceededException;
	
	public void updateCourse(String ttid, Timetable timetable) throws TimeTableCollectionException;
	
	public void deleteByTtid(String ttid) throws TimeTableCollectionException;
}
