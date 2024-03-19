package com.timetable.universityTimetable.exception;

public class FacultyCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FacultyCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String facultyCode) {
		return "Course with " + facultyCode+" not found";
	}
	
	public static String facultyAlreadyExist() {
		return "Faculty with given Faculty Code already exist";
	}

}
