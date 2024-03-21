package com.timetable.universityTimetable.controller;

import java.util.ArrayList;
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

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Enrollment;
import com.timetable.universityTimetable.modelclass.Timetable;
import com.timetable.universityTimetable.repository.EnrollmentRepository;
import com.timetable.universityTimetable.repository.TimeTableRepository;
import com.timetable.universityTimetable.service.TimeTableService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class TimeTableController {
	
	@Autowired
	private TimeTableRepository timeTableRepo;
	
	@Autowired
	private TimeTableService timeTableService;

	@Autowired
	private EnrollmentRepository enrollRepo;

	
	@PostMapping("/Timetable")
	public ResponseEntity<?> createFaculty(@RequestBody @Valid  Timetable timetable,  BindingResult result) {
		if (result.hasErrors()) {
	        // If there are validation errors, return a response with the error details
	        List<String> errors = result.getAllErrors().stream()
	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
		
//		try {
//	    	timeTableService.createTimeTable(timetable);
//	       
//	    	return ResponseEntity.ok().body(Map.of("message", "Timetable successfully saved", "success", true));
//	    } catch (ConstraintViolationException e) {
//	        return new ResponseEntity<>("Error creating Timetable: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//	    }catch (TimeTableCollectionException e) {
//			//return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
//	    	return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(Map.of("message", "Timetable saved unsuccessful: " + e.getMessage(), "success", false));
//		}
		try {
            timeTableService.createTimeTable(timetable);
            return new ResponseEntity<>(timetable, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TimeTableCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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
	
	 @GetMapping("/student-timetables/{studId}")
	    public ResponseEntity<?> getStudentTimetables(@PathVariable("studentId") String studentId) {
	        try {
	            // Get the enrolled courses of the student from the enrollment repository
	            List<Enrollment> enrollments = enrollRepo.existingCourses(studentId);
	            
	            // Initialize a list to store timetables for enrolled courses
	            List<Timetable> studentTimetables = new ArrayList<>();
	            
	            // Iterate through the enrolled courses
	            for (Enrollment enrollment : enrollments) {
	                // Get the course details for each enrollment
	              //  Optional<String> optionalCourse = Optional.ofNullable(enrollment.getCourseId());
	   
	                    
	                    // Get the timetables for the course
	                    List<Timetable> courseTimetables = timeTableRepo.findByTimetableCourseId(enrollment.getCourseCode());
	                    
	                    // Add the course timetables to the list of student timetables
	                    studentTimetables.addAll(courseTimetables);
	                
	            }
	            
	            // Check if any timetables were found for the student's enrolled courses
	            if (!studentTimetables.isEmpty()) {
	                return new ResponseEntity<>(studentTimetables, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("No timetables available for the student's enrolled courses", HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
}
