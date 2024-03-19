package com.timetable.universityTimetable.exception;

public class TimeTableCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TimeTableCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String ttid) {
		return "Course with " + ttid+" not found";
	}
	
	public static String TimeTableAlreadyExist() {
		return "Course with given Course Id already exist";
	}

}
