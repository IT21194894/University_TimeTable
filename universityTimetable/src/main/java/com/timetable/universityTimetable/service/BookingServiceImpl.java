package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.BookingRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class BookingServiceImpl implements BookingService {
	
	@Autowired
	public BookingRepository bookingRepo;

	@Override
	public void createBooking(Booking booking) throws ConstraintViolationException, UniTimetableCollectionException {
		 Optional<Booking> bookingOptional = bookingRepo.findByBookId(booking.getBookid());
	        if (bookingOptional.isPresent()) {
	            throw new UniTimetableCollectionException(UniTimetableCollectionException.BookingAlreadyExist());
	        } else {
	        	booking.setCreatedAt(new Date(System.currentTimeMillis()));
	            bookingRepo.save(booking);
	        }

	}

	@Override
	public List<Booking> getAllBookings() {
		List<Booking> bookings= bookingRepo.findAll();
		if(bookings.size()>0) {
			return bookings;
		}else {
			return new ArrayList<Booking>();
		}
	}

	@Override
	public Booking getBooking(String bookid) throws UniTimetableCollectionException {
		 Optional<Booking> bookingOpt = bookingRepo.findById(bookid);
	        if (!bookingOpt.isPresent()) {
	            throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(bookid));
	        } else {
	            return bookingOpt.get();
	        }
	}

	@Override
	public void updateBooking(String bookid, Booking booking) throws UniTimetableCollectionException {
		Optional<Booking> bookingOpt = bookingRepo.findById(bookid);
	    if (bookingOpt.isPresent()) {
	        Booking updatedBooking = bookingOpt.get();
	        updatedBooking.setClassCode(booking.getClassCode()); // Replace "Field1" with the actual field name to be updated
	        updatedBooking.setDay(booking.getDay()); // Replace "Field2" with the actual field name to be updated
	        updatedBooking.setStartTime(booking.getStartTime()); 
	        updatedBooking.setEndTime(booking.getEndTime()); 
	        // Repeat the above for all fields that need to be updated
	        updatedBooking.setUpdatedAt  (new Date(System.currentTimeMillis()));
	        bookingRepo.save(updatedBooking);
	    } else {
	        throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(bookid));
	    }

	}

	@Override
	public void deleteBooking(String bookid) throws UniTimetableCollectionException {
		 Optional<Booking> bookingOpt = bookingRepo.findById(bookid);
		    if (bookingOpt.isPresent()) {
		        bookingRepo.deleteById(bookid);
		    } else {
		        throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(bookid));
		    }

	}

}
