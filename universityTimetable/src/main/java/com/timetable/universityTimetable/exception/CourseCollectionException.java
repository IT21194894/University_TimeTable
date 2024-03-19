package com.timetable.universityTimetable.exception;

public class CourseCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String courseCode) {
		return "Course with " + courseCode+" not found";
	}
	
	public static String CourseAlreadyExist() {
		return "Course with given Course Id already exist";
	}
}
