package com.timetable.universityTimetable.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.exception.UserCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.UserRepository;
import com.timetable.universityTimetable.service.CourseService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class CourseController {
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private CourseService courseService;

	
	@GetMapping("/course")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllUsers() {

		List<Course> courses=courseService.getAllCourses();
		return new ResponseEntity<>(courses,courses.size()>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
			
	}
//	@PostMapping("/course")
//    public ResponseEntity<?> createCourse(@RequestBody @Valid Course course, @RequestParam String facultyCode, BindingResult result) {
//        if (result.hasErrors()) {
//            // If there are validation errors, return a response with the error details
//            List<String> errors = result.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//            courseService.createCourse(course, facultyCode); // Pass the facultyCode to the service method
//            return ResponseEntity.ok().body(Map.of("message", "Course assigned successfully", "success", true));
//        } catch (ConstraintViolationException e) {
//            return new ResponseEntity<>("Error creating course: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//        } catch (CourseCollectionException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(Map.of("message", "Course assigned unsuccessful: " + e.getMessage(), "success", false));
//        } catch (FacultyCollectionException e) { // Handle FacultyCollectionException
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "Error creating course: " + e.getMessage(), "success", false));
//        }
//    }
	@PostMapping("/course")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createCourse(@RequestBody @Valid  Course course,  BindingResult result) {
		if (result.hasErrors()) {
	        // If there are validation errors, return a response with the error details
	        List<String> errors = result.getAllErrors().stream()
	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
		try {
			courseService.createCourse(course);
	        //return new ResponseEntity<User>(user, HttpStatus.OK);
	    	return ResponseEntity.ok().body(Map.of("message", "Course registered successfully", "success", true));
	    } catch (ConstraintViolationException e) {
	        return new ResponseEntity<>("Error creating Course: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	    }catch (CourseCollectionException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	    	//return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User registration unsuccessful: " + e.getMessage(), "success", false));
	    	
	    }
	}

	
	@GetMapping("/course/{cid}")
	public ResponseEntity<?> getSingleCourse(@PathVariable("cid") String cid) {
		try {
			return new ResponseEntity<>(courseService.getCourse(cid), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "success", false));
		}
	}
	
	@PutMapping("/course/{cid}")
	public ResponseEntity<?> updateCourseByCid(@PathVariable("cid") String cid, @RequestBody Course course) {
		try {
			courseService.updateCourse(cid, course);
			//return new ResponseEntity<>("Update User with ID : "+id, HttpStatus.OK);
			return ResponseEntity.ok().body(Map.of("message", "Course updated successfully", "success", true));
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>("Error updating course: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}catch (CourseCollectionException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Course registration unsuccessful: " + e.getMessage(), "success", false));
		}	
	}
	
	@DeleteMapping("/course/{courseCode}")
	public ResponseEntity<?> deleteCourseByCid(@PathVariable("courseCode") String courseCode) {
		try {
			courseService.deleteByCourseCode(courseCode);
	        return new ResponseEntity<>("Course with ID " + courseCode + " deleted successfully", HttpStatus.OK);
		} catch (CourseCollectionException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
