package com.timetable.universityTimetable.service;

import java.util.List;

import com.timetable.universityTimetable.exception.UserCollectionException;
import com.timetable.universityTimetable.modelclass.User;

import jakarta.validation.ConstraintViolationException;

public interface UserService {

	public void createUser(User user)throws ConstraintViolationException, UserCollectionException;
	
	public List<User> getAllUsers();
	
	public User getUser(String id) throws UserCollectionException;
	
	public void updateUser(String id, User user) throws UserCollectionException;
	
	public void deleteUserById(String id) throws UserCollectionException;
	
}
