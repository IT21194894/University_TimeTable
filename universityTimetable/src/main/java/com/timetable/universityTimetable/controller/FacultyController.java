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

import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.repository.FacultyRepository;
import com.timetable.universityTimetable.service.FacultyService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
public class FacultyController {
	@Autowired
	private FacultyRepository facultyRepo;
	
	@Autowired
	private FacultyService facService;

	
	@GetMapping("/Faculty")
	public ResponseEntity<?> getFaculties() {

		List<Faculty> faculties=facService.getFaculties();
		return new ResponseEntity<>(faculties,faculties.size()>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
			
	}
	@PostMapping("/Faculty")
	public ResponseEntity<?> createFaculty(@RequestBody @Valid  Faculty faculty,  BindingResult result) {
		if (result.hasErrors()) {
	        // If there are validation errors, return a response with the error details
	        List<String> errors = result.getAllErrors().stream()
	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
		
		try {
	    	facService.createFaculty(faculty);
	       
	    	return ResponseEntity.ok().body(Map.of("message", "Faculty registered successfully", "success", true));
	    } catch (ConstraintViolationException e) {
	        return new ResponseEntity<>("Error creating Faculty: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	    }catch (FacultyCollectionException e) {
			//return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	    	return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Faculty registration unsuccessful: " + e.getMessage(), "success", false));
		}
	}
	
	@GetMapping("/Faculty/{fid}")
	public ResponseEntity<?> getSingleFaculty(@PathVariable("fid") String fid) {
		try {
			return new ResponseEntity<>(facService.getFaculty(fid), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "success", false));
		}
	}
	
	@PutMapping("/Faculty/{fid}")
	public ResponseEntity<?> updateFacultyByFid(@PathVariable("fid") String fid, @RequestBody Faculty faculty) {
		try {
			facService.updateFaculty(fid, faculty);
			//return new ResponseEntity<>("Update Faculty with ID : "+id, HttpStatus.OK);
			return ResponseEntity.ok().body(Map.of("message", "Faculty updated successfully", "success", true));
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>("Error updating Faculty: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}catch (FacultyCollectionException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Faculty registration unsuccessful: " + e.getMessage(), "success", false));
		}	
	}
	
	@DeleteMapping("/Faculty/{facultyCode}")
	public ResponseEntity<?> deleteFacultyByFid(@PathVariable("facultyCode") String facultyCode) {
		try {
			facService.deleteByFacultyCode(facultyCode);
	        return new ResponseEntity<>("Faculty with ID " + facultyCode + " deleted successfully", HttpStatus.OK);
		} catch (FacultyCollectionException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
