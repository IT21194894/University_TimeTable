package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.exception.UserCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.FacultyRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class CourseServiceImpl implements CourseService {


		@Autowired
		public CourseRepository courseRepo;
		
		@Autowired
		public FacultyRepository facultyRepo;
		 
//		@Override
//	    public void createCourse(Course course, String facultyCode) throws ConstraintViolationException, CourseCollectionException, FacultyCollectionException {
//
//	        Optional<Faculty> optionalFaculty = facultyRepo.findByFacultyCode(facultyCode);
//	        if (optionalFaculty.isPresent()) {
//	            Optional<Course> courseOptional = courseRepo.findByCourseCode(course.getCourseCode());
//	            if (courseOptional.isPresent()) {
//	                throw new CourseCollectionException(CourseCollectionException.CourseAlreadyExist());
//	            } else {
//	                course.setCreatedAt(new Date(System.currentTimeMillis()));
//	                courseRepo.save(course);
//	            }
//	        } else {
//	            throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(facultyCode));
//	        }
//	    }
		
		public void createCourse(Course course) throws ConstraintViolationException, CourseCollectionException {
		
	        Optional<Course> courseOptional = courseRepo.findByCourseCode(course.getCourseCode());
	        if (courseOptional.isPresent()) {
	            throw new CourseCollectionException(CourseCollectionException.CourseAlreadyExist());
	        } else {
	            course.setCreatedAt(new Date(System.currentTimeMillis()));
	            courseRepo.save(course);
	        }
	    }

		@Override
		public List<Course> getAllCourses() {
			List<Course> courses= courseRepo.findAll();
			if(courses.size()>0) {
				return courses;
			}else {
				return new ArrayList<Course>();
			}
		}

		@Override
		public Course getCourse(String courseCode) throws CourseCollectionException {
			Optional<Course> courseOpt = courseRepo.findByCourseCode(courseCode);
			if(!courseOpt.isPresent()) {
				throw new CourseCollectionException(CourseCollectionException.NotFoundException(courseCode));
			}else {
				return courseOpt.get();
			}
		}

		@Override
		public void updateCourse(String cid, Course course) throws CourseCollectionException {
			
			Optional<Course> courseOptId = courseRepo.findById(cid);
			Optional<Course> courseOptCode = courseRepo.findByCourseCode(course.getCourseCode());
			if (courseOptId.isPresent()) {
	        	if(courseOptCode.isPresent()&& !courseOptCode.get().getCid().equals(cid)) {
	        		throw new CourseCollectionException(CourseCollectionException.CourseAlreadyExist());
	        	}
	        	
	            Course courseUpdate= courseOptId.get();
	            
	            courseUpdate.setCourseName(course.getCourseName());
	            courseUpdate.setCredit(course.getCredit());
	            courseUpdate.setDescription(course.getDescription());
	            courseUpdate.setFaculty(course.getFaculty());
	            courseUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
	            courseRepo.save(courseUpdate);
	        } else {
	        	throw new CourseCollectionException(CourseCollectionException.NotFoundException(cid));
	            
	        }  
	        }

		

		@Override
		public void deleteByCourseCode(String courseCode) throws CourseCollectionException {
			Optional<Course> courseOpt = courseRepo.findByCourseCode(courseCode);
			
			if(!courseOpt.isPresent()) {
				throw new CourseCollectionException(CourseCollectionException.NotFoundException(courseCode));
			}else {
				courseRepo.deleteByCourseCode(courseCode);
			}

		}

	}



