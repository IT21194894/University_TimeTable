package com.timetable.universityTimetable.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.timetable.universityTimetable.modelclass.Enrollment;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.EnrollmentRepository;
import com.timetable.universityTimetable.repository.UserRepository;
import com.timetable.universityTimetable.security.service.UserDetailsImpl;
import com.timetable.universityTimetable.service.EnrollmentService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class EnrollmentController {
	 @Autowired
	    private EnrollmentRepository enrollmentRepo;
	    
	    @Autowired
	    private EnrollmentService enrollmentService;
	    
	    @Autowired
	    private UserRepository studentRepository;
	    
	    @Autowired
	    private CourseRepository courseRepository;
	    
	    @GetMapping("/enrollment")
	    public ResponseEntity<?> getAllEnrollments() {
	        UserDetailsImpl userDetails = getAuthenticatedUser();

	        // Check if the user is a student
	        if (isStudent(userDetails)) {
	            List<Enrollment> enrollments = enrollmentRepo.findByStudId(userDetails.getId());
	            return ResponseEntity.ok(enrollments);
	        } else if (isAdmin(userDetails)){
	            List<Enrollment> enrollments = enrollmentRepo.findAll();
	            return ResponseEntity.ok(enrollments);
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                                 .body("Unauthorized access"); // Return an unauthorized error message
	        }
	    }

//	    @PostMapping("/enrollment")
//	    public ResponseEntity<?> createEnrollment(@RequestBody @Valid Enrollment enrollment, BindingResult result) {
//	        if (result.hasErrors()) {
//	            // If there are validation errors, return a response with the error details
//	            List<String> errors = result.getAllErrors().stream()
//	                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//	                    .collect(Collectors.toList());
//	            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	        }
//
//	        try {
//	            enrollmentService.createEnrollment(enrollment);
//	            return ResponseEntity.ok().body(Map.of("message", "Enrollment created successfully", "success", true));
//	        } catch (ConstraintViolationException e) {
//	            return new ResponseEntity<>("Error creating enrollment: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//	        } catch (UniTimetableCollectionException e) {
//	        	return ResponseEntity.status(HttpStatus.CONFLICT)
//	                    .body(Map.of("message", "Enrollment unsuccessful: " + e.getMessage(), "success", false));
//	        } 
//	        
//	    }

	    
	    @PostMapping("/enrollment")
	    public ResponseEntity<?> createEnrollment(@RequestBody @Valid Enrollment enrollment, BindingResult result) {
	        if (result.hasErrors()) {
	            List<String> errors = result.getAllErrors().stream()
	                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                    .collect(Collectors.toList());
	            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	        }

	        try {
	            enrollmentService.createEnrollment(enrollment);
	            return ResponseEntity.ok().body(Map.of("message", "Enrollment created successfully", "success", true));
	        } catch (ConstraintViolationException e) {
	            return new ResponseEntity<>("Error creating enrollment: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	        } catch (UniTimetableCollectionException e) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body(Map.of("message", "Enrollment unsuccessful: " + e.getMessage(), "success", false));
	        }
	    }

//	    private UserDetailsImpl getAuthenticatedUser() {
//	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	        if (principal instanceof UserDetails) {
//	            return (UserDetailsImpl) principal;
//	        } else {
//	            throw new UsernameNotFoundException("User not authenticated");
//	        }
//	    }
	    @GetMapping("/enrollment/{enrollId}")
	    public ResponseEntity<?> getEnrollment(@PathVariable("enrollmentId") String enrollmentId) {
	        try {
	            return new ResponseEntity<>(enrollmentService.getEnrollment(enrollmentId), HttpStatus.OK);
	        } catch (UniTimetableCollectionException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "success", false));
	        }
	    }
//
//	    @PutMapping("/enrollment/{enrollId}")
//	    public ResponseEntity<?> updateEnrollment(@PathVariable("enrollId") String enrollId, @RequestBody Enrollment enrollment) {
//	        try {
//	            enrollmentService.updateEnrollment(enrollId, enrollment);
//	            return ResponseEntity.ok().body(Map.of("message", "Enrollment updated successfully", "success", true));
//	        } catch (ConstraintViolationException e) {
//	            return new ResponseEntity<>("Error updating enrollment: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//	        } catch (UniTimetableCollectionException e) {
//	            return ResponseEntity.status(HttpStatus.CONFLICT)
//	                    .body(Map.of("message", "Enrollment updating unsuccessful: " + e.getMessage(), "success", false));
//	        }
//	    }

	    @DeleteMapping("/enrollment/{enrollId}")
	    public ResponseEntity<?> deleteEnrollment(@PathVariable("enrollId") String enrollId) {
	        try {
	            enrollmentService.deleteEnrollment(enrollId);
	            return new ResponseEntity<>("Enrollment with ID " + enrollId + " deleted successfully", HttpStatus.OK);
	        } catch (UniTimetableCollectionException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	        }
	    }
	    
	    private UserDetailsImpl getAuthenticatedUser() {
		    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    if (principal instanceof UserDetails) {
		        return (UserDetailsImpl) principal;
		    } else {
		        throw new UsernameNotFoundException("User not authenticated");
		    }
		}
		
		private boolean isStudent(UserDetailsImpl userDetails) {
		    return userDetails.getAuthorities().stream()
		            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT"));
		}
		private boolean isAdmin(UserDetailsImpl userDetails) {
		    return userDetails.getAuthorities().stream()
		            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
		}
	    
	    
}
