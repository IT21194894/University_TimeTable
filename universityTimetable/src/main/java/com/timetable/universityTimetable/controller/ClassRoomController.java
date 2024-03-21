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

import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.exception.UserCollectionException;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.ClassRoomMgtAndBooikingRepository;
import com.timetable.universityTimetable.repository.UserRepository;
import com.timetable.universityTimetable.service.ClassroomMgtAndBookingService;
//import com.timetable.universityTimetable.service.UserService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ClassRoomController {
	
	@Autowired
	private ClassRoomMgtAndBooikingRepository classRepo;
	
	@Autowired
	private ClassroomMgtAndBookingService classService;

	@GetMapping("/classroom")
	public ResponseEntity<?> getAllClassRooms() {

		List<Classroom> classRooms=classService.getAllClassRooms();
		return new ResponseEntity<>(classRooms,classRooms.size()>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
			
	}
	@PostMapping("/classroom")
	public ResponseEntity<?> createUser(@RequestBody @Valid  Classroom classRooms,  BindingResult result) {
		if (result.hasErrors()) {
	        // If there are validation errors, return a response with the error details
	        List<String> errors = result.getAllErrors().stream()
	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
		
		try {
	    	classService.createClassroom(classRooms);
	        //return new ResponseEntity<User>(user, HttpStatus.OK);
	    	return ResponseEntity.ok().body(Map.of("message", "Class Room added successfully", "success", true));
	    } catch (ConstraintViolationException e) {
	        return new ResponseEntity<>("Error entering Class Room: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	    }catch (UniTimetableCollectionException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	    	//return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User registration unsuccessful: " + e.getMessage(), "success", false));
	    	
	    }
	}
	
	@GetMapping("/classroom/{classid}")
	public ResponseEntity<?> getClassroom(@PathVariable("classid") String classid) {
		try {
			return new ResponseEntity<>(classService.getClassroom(classid), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "success", false));
		}
	}
	
	@PutMapping("/classroom/{classid}")
	public ResponseEntity<?> updateClassById(@PathVariable("classid") String classid, @RequestBody Classroom classroom) {
		try {
			classService.updateClassroom(classid, classroom);
			//return new ResponseEntity<>("Update User with ID : "+id, HttpStatus.OK);
			return ResponseEntity.ok().body(Map.of("message", "classroom updated successfully", "success", true));
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>("Error updating classroom: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}catch (UniTimetableCollectionException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "classroom updating unsuccessful: " + e.getMessage(), "success", false));
		}	
	}
	
	@DeleteMapping("/classroom/{classid}")
	public ResponseEntity<?> deleteUserById(@PathVariable("classid") String classRoomCode) {
		try {
			classService.deleteByClassroomCode(classRoomCode);
	        return new ResponseEntity<>("class Room with classRoomCode " + classRoomCode + " deleted successfully", HttpStatus.OK);
		} catch (UniTimetableCollectionException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}



}
