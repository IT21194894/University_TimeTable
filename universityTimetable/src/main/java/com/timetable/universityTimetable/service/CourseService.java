package com.timetable.universityTimetable.service;

import java.util.List;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.modelclass.Course;

import jakarta.validation.ConstraintViolationException;

public interface CourseService {
	public void createCourse(Course course, String facultyCode)throws ConstraintViolationException, CourseCollectionException,FacultyCollectionException;
	public List<Course> getAllCourses();
	
	public Course getCourse(String courseCode) throws CourseCollectionException;
	
	public void updateCourse(String courseCode, Course course) throws CourseCollectionException;
	
	public void deleteByCourseCode(String courseCode) throws CourseCollectionException;
}
