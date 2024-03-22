package com.timetable.universityTimetable.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
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
	        
	        boolean isAvailable = checkClassroomAvailability(booking.getClassCode(), booking.getStartTime(), booking.getEndTime(), booking.getDay());
	        if (!isAvailable) {
	            throw new UniTimetableCollectionException("Classroom is not available during the specified time.");
	        }
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
	    if (existingRoom.isPresent()) {
	        // Check if the classroom is available for booking
	        boolean isAvailable = checkClassroomAvailability(booking.getClassCode(), booking.getStartTime(), booking.getEndTime(), booking.getDay());
	        if (!isAvailable) {
	            throw new UniTimetableCollectionException("Classroom " + booking.getClassCode() + " is not available at the given time slot");
	        }
	        // Proceed with booking
	        booking.setCreatedAt(new Date(System.currentTimeMillis()));
	        bookingRepo.save(booking);
	        
	    } else {
	        throw new UniTimetableCollectionException("Room not found");
	    }
	}


	@Override
	public boolean checkClassroomAvailability(String classroomName, String startTimeStr, String endTimeStr,
			String dayOfWeekStr) {
		 // Parse the start time, end time, and day of the week strings into appropriate objects
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);
      //  String dayOfWeek = dayOfWeekStr.toUpperCase();

        // Query the database to fetch existing bookings for the given classroom and day of the week
        List<Booking> existingBookings = bookingRepo.findByClassroomNameAndDayOfWeek(classroomName, dayOfWeekStr);


        // Check for conflicts with existing bookings
        for (Booking booking : existingBookings) {
            // Parse booking start time and end time
            LocalTime bookingStartTime = LocalTime.parse(booking.getStartTime());
            LocalTime bookingEndTime = LocalTime.parse(booking.getEndTime());

            // Check if the booking is on the same day of the week
            if (!booking.getDay().equalsIgnoreCase(dayOfWeekStr)) {
                continue; // Skip if booking is not on the same day of the week
            }

            // Check for overlap between the time intervals
            if (startTime.isBefore(bookingEndTime) && endTime.isAfter(bookingStartTime)) {
                // Conflict found
                return false;
            }
        }

        // No conflicts found, classroom is available
        return true;
	}

}
