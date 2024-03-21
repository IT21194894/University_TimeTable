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
		return "TimeTable with " + ttid+" not found";
	}
	
	public static String TimeTableAlreadyExist() {
		return "TimeTable Id already exist";
	}

}
