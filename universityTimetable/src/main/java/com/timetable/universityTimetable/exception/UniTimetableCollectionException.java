package com.timetable.universityTimetable.exception;

public class UniTimetableCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UniTimetableCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String id) {
		return id+" not found";
	}
	
	public static String ClassRoomAlreadyExist() {
		return "ClassRoom with given id already exist";
	}
	public static String BookingAlreadyExist() {
		return "Booking with given id already exist";
	}
	
	public static String EnrollmentAlreadyExist() {
		return "Student Already Enrolled";
	}
	

}
