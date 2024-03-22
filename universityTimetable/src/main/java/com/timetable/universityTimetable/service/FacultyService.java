package com.timetable.universityTimetable.service;

import java.util.List;

import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.modelclass.Faculty;

import jakarta.validation.ConstraintViolationException;

public interface FacultyService {
	public void createFaculty(Faculty faculty)throws ConstraintViolationException, FacultyCollectionException;
	
	public List<Faculty> getFaculties();
	
	public Faculty getFaculty(String facultyCode) throws FacultyCollectionException;
	
	public void updateFaculty(String fid, Faculty faculty) throws FacultyCollectionException;
	
	public void deleteByFacultyCode(String facultyCode) throws FacultyCollectionException;
	
	public List<String> getAssignedCoursesForUsername(String facultyUserName);
	
	public List<String> getAssignedFacultyForCourse(String courseCode) ;
}
