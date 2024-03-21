//package com.timetable.universityTimetable.controller;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.DefaultMessageSourceResolvable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.timetable.universityTimetable.exception.UserCollectionException;
//import com.timetable.universityTimetable.modelclass.User;
//import com.timetable.universityTimetable.repository.UserRepository;
//import com.timetable.universityTimetable.service.UserService;
//
//import jakarta.validation.ConstraintViolationException;
//import jakarta.validation.Valid;
//
//
//@RestController
//public class UserController {
//	
//	@Autowired
//	private UserRepository userRepo;
//	
//	@Autowired
//	private UserService userService;
//
//	
//	@GetMapping("/user")
//	public ResponseEntity<?> getAllUsers() {
//
//		List<User> users=userService.getAllUsers();
//		return new ResponseEntity<>(users,users.size()>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
//			
//	}
//	@PostMapping("/user")
//	public ResponseEntity<?> createUser(@RequestBody @Valid  User user,  BindingResult result) {
//		if (result.hasErrors()) {
//	        // If there are validation errors, return a response with the error details
//	        List<String> errors = result.getAllErrors().stream()
//	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//	                .collect(Collectors.toList());
//	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	    }
//		
//		try {
//	    	userService.createUser(user);
//	        //return new ResponseEntity<User>(user, HttpStatus.OK);
//	    	return ResponseEntity.ok().body(Map.of("message", "User registered successfully", "success", true));
//	    } catch (ConstraintViolationException e) {
//	        return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//	    }catch (UserCollectionException e) {
//			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
//	    	//return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User registration unsuccessful: " + e.getMessage(), "success", false));
//	    	
//	    }
//	}
//	
//	@GetMapping("/user/{id}")
//	public ResponseEntity<?> getSingleUser(@PathVariable("id") String id) {
//		try {
//			return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "success", false));
//		}
//	}
//	
//	@PutMapping("/user/{id}")
//	public ResponseEntity<?> updateUserById(@PathVariable("id") String id, @RequestBody User user) {
//		try {
//			userService.updateUser(id, user);
//			//return new ResponseEntity<>("Update User with ID : "+id, HttpStatus.OK);
//			return ResponseEntity.ok().body(Map.of("message", "User registered successfully", "success", true));
//		} catch (ConstraintViolationException e) {
//			return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//		}catch (UserCollectionException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(Map.of("message", "User registration unsuccessful: " + e.getMessage(), "success", false));
//		}	
//	}
//	
//	@DeleteMapping("/user/{id}")
//	public ResponseEntity<?> deleteUserById(@PathVariable("id") String id) {
//		try {
//			userService.deleteUserById(id);
//	        return new ResponseEntity<>("User with ID " + id + " deleted successfully", HttpStatus.OK);
//		} catch (UserCollectionException e) {
//	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		}
//	}
//
//
//}
