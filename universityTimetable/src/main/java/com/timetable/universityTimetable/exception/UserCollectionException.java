package com.timetable.universityTimetable.exception;

import org.apache.logging.log4j.message.Message;

public class UserCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String id) {
		return "User with " + id+" not found";
	}
	
	public static String UserAlreadyExist() {
		return "User with given NIC already exist";
	}
}
