package com.timetable.universityTimetable.service;

import java.util.List;

import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;

import jakarta.validation.ConstraintViolationException;

public interface BookingService {
public void createBooking(Booking booking)throws ConstraintViolationException, UniTimetableCollectionException;
	
	public List<Booking> getAllBookings();
	
	public Booking getBooking(String bookid) throws UniTimetableCollectionException;
	
	public void updateBooking(String bookid, Booking booking) throws UniTimetableCollectionException;
	
	public void deleteBooking(String bookid) throws UniTimetableCollectionException;
}
