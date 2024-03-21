package com.timetable.universityTimetable.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.service.BookingService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class BookingController {
	 @Autowired
	    private BookingRepository bookingRepo;
	    
	    @Autowired
	    private BookingService bookingService;
	    
	    @GetMapping("/bookings")
	    public ResponseEntity<?> getAllBookings() {
	    	List<Booking> bookings=bookingService.getAllBookings();
	        return new ResponseEntity<>(bookings, bookings.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	    }
	    
	    @PostMapping("/bookings")
	    public ResponseEntity<?> createBooking(@RequestBody @Valid Booking booking, BindingResult result) {
	    	if (result.hasErrors()) {
		        // If there are validation errors, return a response with the error details
		        List<String> errors = result.getAllErrors().stream()
		                .map(DefaultMessageSourceResolvable::getDefaultMessage)
		                .collect(Collectors.toList());
		        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		    }
			try {
				bookingService.createBooking(booking);
		        //return new ResponseEntity<User>(user, HttpStatus.OK);
		    	return ResponseEntity.ok().body(Map.of("message", "Booking added successfully", "success", true));
		    } catch (ConstraintViolationException e) {
		        return new ResponseEntity<>("Error creating Booking: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		    }catch (UniTimetableCollectionException e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
		    	//return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User registration unsuccessful: " + e.getMessage(), "success", false));
		    	
		    }
	    }
	    
	    @GetMapping("/bookings/{bookingId}")
	    public ResponseEntity<?> getBooking(@PathVariable("bookingId") String bookingId) {
	        try {
	            return new ResponseEntity<>(bookingService.getBooking(bookingId), HttpStatus.OK);
	        } catch (UniTimetableCollectionException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(Map.of("message", e.getMessage(), "success", false));
	        }
	    }
	    
	    @PutMapping("/bookings/{bookingId}")
	    public ResponseEntity<?> updateBooking(@PathVariable("bookingId") String bookingId, @RequestBody Booking booking) {
	        try {
	            bookingService.updateBooking(bookingId, booking);
	            return ResponseEntity.ok().body(Map.of("message", "Booking updated successfully", "success", true));
	        } catch (ConstraintViolationException e) {
	            return new ResponseEntity<>("Error updating booking: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	        } catch (UniTimetableCollectionException e) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body(Map.of("message", "Booking updating unsuccessful: " + e.getMessage(), "success", false));
	        }
	    }
	    
	    @DeleteMapping("/bookings/{bookingId}")
	    public ResponseEntity<?> deleteBookingById(@PathVariable("bookingId") String bookingId) {
	        try {
	            bookingService.deleteBooking(bookingId);
	            return new ResponseEntity<>("Booking with ID " + bookingId + " deleted successfully", HttpStatus.OK);
	        } catch (UniTimetableCollectionException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	        }
	    }
}
