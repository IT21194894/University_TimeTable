package com.timetable.universityTimetable.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.repository.ClassRoomRepository;
import com.timetable.universityTimetable.service.BookingService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class BookingController {
	 	@Autowired
	    private BookingRepository bookingRepo;
	 
	 	@Autowired
	    private ClassRoomRepository classRoomRepo;
	    
	    @Autowired
	    private BookingService bookingService;
	    
	    @GetMapping("/bookings")
	    public ResponseEntity<?> getAllBookings() {
	    	List<Booking> bookings=bookingService.getAllBookings();
	        return new ResponseEntity<>(bookings, bookings.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	    }
	    
	    @PostMapping("/bookings")
		public  ResponseEntity<?>  createBooking(@RequestBody @Valid  Booking booking, BindingResult result) throws ConstraintViolationException, UniTimetableCollectionException {
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
		        return new ResponseEntity<>("Error entering Booking: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		    }catch (UniTimetableCollectionException e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
		    	//return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User registration unsuccessful: " + e.getMessage(), "success", false));
		    	
		    }

		}
		
		public boolean checkClassroomAvailability(String classroomName, String startTimeStr, String endTimeStr, String dayOfWeekStr) {
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
	    
	    @GetMapping("/available-classrooms")
		  public ResponseEntity<?> getAvailableClassrooms(@RequestParam String startTime,
		                                                  @RequestParam String endTime,
		                                                  @RequestParam String dayOfWeek) {
		      LocalTime startTimeCn = LocalTime.parse(startTime);
		      LocalTime endTimeCn = LocalTime.parse(endTime);

		      try {
		          // Get the list of booked rooms for the specified time slot and day
		          List<Booking> bookedRooms = bookingRepo.findAvailableRoomsByDay(dayOfWeek);

		          List<Classroom> Rooms = classRoomRepo.findAll();
		          
		          // Find available rooms based on the list of booked rooms
		          Iterator<Booking> iterator = bookedRooms.iterator();
		          while (iterator.hasNext()) {
		              Booking booking = iterator.next();
		              // Parse booking start time and end time
		              LocalTime bookingStartTime = LocalTime.parse(booking.getStartTime());
		              LocalTime bookingEndTime = LocalTime.parse(booking.getEndTime());

		              if (startTimeCn.isBefore(bookingEndTime) && endTimeCn.isAfter(bookingStartTime)) {
		                  // Conflict found, remove the booked room
		                  iterator.remove();
		              }
		          }

		          List<String> RoomNames = new ArrayList<>();
		          for (Booking availableRoom : bookedRooms) {
		              RoomNames.add(availableRoom.getClassCode());
		          }

		          List<Classroom> availableRooms = Rooms.stream()
		                                           .filter(room -> !RoomNames.contains(room.getClassroomCode()))
		                                           .collect(Collectors.toList());

		          if (!availableRooms.isEmpty()) {
		              return new ResponseEntity<>(availableRooms, HttpStatus.OK);
		          } else {
		              return new ResponseEntity<>("No available classrooms for the specified time slot and day", HttpStatus.NOT_FOUND);
		          }
		      } catch (Exception e) {
		          return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		      }
		  }
}
