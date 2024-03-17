package com.timetable.universityTimetable.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/timetable")
	public ResponseEntity<?> getAllUsers() {
		
		List<User> users=userRepo.findAll();
		if(users.size()>0) {
			return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		}else {
			return new ResponseEntity<>("No User available", HttpStatus.NOT_FOUND);
		}
		
	}
}
