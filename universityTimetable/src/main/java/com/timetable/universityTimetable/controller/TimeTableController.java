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
import org.springframework.web.bind.annotation.RestController;

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Timetable;
import com.timetable.universityTimetable.repository.TimeTableRepository;
import com.timetable.universityTimetable.service.TimeTableService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class TimeTableController {
	
	@Autowired
	private TimeTableRepository timeTableRepo;
	
	@Autowired
	private TimeTableService timeTableService;

	
	@GetMapping("/Timetable")
	public ResponseEntity<?> getFaculties() {

		List<Timetable> timetables=timeTableService.getAllTimetables();
		return new ResponseEntity<>(timetables,timetables.size()>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
			
	}
	@PostMapping("/Timetable")
	public ResponseEntity<?> createFaculty(@RequestBody @Valid  Timetable timetable,  BindingResult result) {
		if (result.hasErrors()) {
	        // If there are validation errors, return a response with the error details
	        List<String> errors = result.getAllErrors().stream()
	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
		
		try {
	    	timeTableService.createTimeTable(timetable);
	       
	    	return ResponseEntity.ok().body(Map.of("message", "Timetable successfully saved", "success", true));
	    } catch (ConstraintViolationException e) {
	        return new ResponseEntity<>("Error creating Timetable: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	    }catch (TimeTableCollectionException e) {
			//return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	    	return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Timetable saved unsuccessful: " + e.getMessage(), "success", false));
		}
	}
	
	@GetMapping("/Timetable/{ttid}")
	public ResponseEntity<?> getSingleTimeTable(@PathVariable("ttid") String ttid) {
		try {
			return new ResponseEntity<>(timeTableService.getTimetable(ttid), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "success", false));
		}
	}
	
	@PutMapping("/Timetable/{ttid}")
	public ResponseEntity<?> updateTimetableByTtid(@PathVariable("ttid") String ttid, @RequestBody Timetable timetable) {
		try {
			timeTableService.updateTimetable(ttid, timetable);
			//return new ResponseEntity<>("Update Faculty with ID : "+id, HttpStatus.OK);
			return ResponseEntity.ok().body(Map.of("message", "Timetable updated successfully", "success", true));
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>("Error updating Timetable: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}catch (TimeTableCollectionException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Timetable update unsuccessful: " + e.getMessage(), "success", false));
		}	
	}
	
	@DeleteMapping("/Timetable/{ttid}")
	public ResponseEntity<?> deleteByTtid(@PathVariable("ttid") String ttid) {
		try {
			timeTableService.deleteByTtid(ttid);
	        return new ResponseEntity<>("Faculty with ID " + ttid + " deleted successfully", HttpStatus.OK);
		} catch (TimeTableCollectionException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
