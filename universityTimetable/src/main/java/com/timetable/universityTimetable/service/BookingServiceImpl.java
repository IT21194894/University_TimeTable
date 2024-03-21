package com.timetable.universityTimetable.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.repository.ClassRoomRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class BookingServiceImpl implements BookingService {
	
	@Autowired
	public BookingRepository bookingRepo;
	
	@Autowired
	public ClassRoomRepository classRoomRepo;

	

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

	@Override
	public void createBooking(Booking booking) throws ConstraintViolationException, UniTimetableCollectionException {
		Optional<Classroom> existingRoom = classRoomRepo.findByClassroomCode(booking.getClassCode());
    	if(existingRoom.isPresent()) {
        // Check if the classroom is available for booking
        boolean isAvailable = checkClassroomAvailability(booking.getClassCode(), booking.getStartTime(), booking.getEndTime(),booking.getDay());
        if (!isAvailable) {
        	throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(booking.getClassCode()));
        }
//        
       
  
     //   booking.setBookedBy();
        // Proceed with booking
        bookingRepo.save(booking);
        booking.setCreatedAt(new Date(System.currentTimeMillis()));
    	}
		
	}

	@Override
	public boolean checkClassroomAvailability(String classroomName, String startTimeStr, String endTimeStr,
			String dayOfWeekStr) {
		// TODO Auto-generated method stub
		return false;
	}

}
