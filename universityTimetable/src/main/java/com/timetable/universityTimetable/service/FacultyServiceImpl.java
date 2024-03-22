package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.ERole;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.FacultyRepository;
import com.timetable.universityTimetable.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class FacultyServiceImpl implements FacultyService {

	@Autowired
	public FacultyRepository facultyRepo;
	
	@Autowired
    public CourseRepository courseRepo;
	
	@Autowired
    public UserRepository userRepo;
	 
	@Override
	public void createFaculty(Faculty faculty)throws ConstraintViolationException, FacultyCollectionException{
	    Optional<Course> course = courseRepo.findByCourseCode(faculty.getCourseCode());
	  //Check1 if the course code available in the course
	    if (!course.isPresent()) {
	        throw new ConstraintViolationException("Course with code " + faculty.getCourseCode() + " not found", null);
	    }
	    Optional<User> userOpt = userRepo.findByUsername(faculty.getFacultyuserName());
	    //Check2 if the user name available in the user
	    if (!userOpt.isPresent()) {
	        throw new ConstraintViolationException("User " + faculty.getFacultyuserName() + " not found", null);
	    } 	
	    User user = userOpt.get();
	    // Check3 if the user's role match the Faculty
	    boolean isFaculty = user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_FACULTY);
	    if (!isFaculty) {
	        throw new FacultyCollectionException("User " + faculty.getFacultyuserName() + " does not have the role of faculty");
	    }
	    // Check4 if the combination of course code and faculty username already exists
	    boolean isDuplicate = facultyRepo.existsByCourseCodeAndFacultyuserName(faculty.getCourseCode(), faculty.getFacultyuserName());
	    if (isDuplicate) {
	        throw new FacultyCollectionException("Course " + faculty.getCourseCode() + " already assigned to " + faculty.getFacultyuserName());
	    }
        
        	faculty.setCreatedAt(new Date(System.currentTimeMillis()));
        	facultyRepo.save(faculty);
	}

	@Override
	public List<Faculty> getFaculties() {
		List<Faculty> faculties= facultyRepo.findAll();
		if(faculties.size()>0) {
			return faculties;
		}else {
			return new ArrayList<Faculty>();
		}
	}

	@Override
	public Faculty getFaculty(String fid) throws FacultyCollectionException {
		Optional<Faculty> facultyOpt = facultyRepo.findById(fid);
		if(!facultyOpt.isPresent()) {
			throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(fid));
		}else {
			return facultyOpt.get();
		}
	}
	@Override
	public List<String> getAssignedCoursesForUsername(String facultyUserName) {
	    // Query the Faculty repository to find all entries with the given facultyUserName
	    List<Faculty> faculties = facultyRepo.findByFacuserName(facultyUserName);
	    
	    // Extract the course codes from each Faculty entry
	    List<String> assignedCourses = faculties.stream()
	            .map(Faculty::getCourseCode)
	            .distinct() // Remove duplicates
	            .collect(Collectors.toList());
	    
	    return assignedCourses;
	}
@Override
	public List<String> getAssignedFacultyForCourse(String courseCode) {
	    // Query the Faculty repository to find all entries with the given courseCode
	    List<Faculty> faculties = facultyRepo.findByCourseCode(courseCode);
	    
	    // Extract the faculty usernames from each Faculty entry
	    List<String> assignedFaculty = faculties.stream()
	            .map(Faculty::getFacultyuserName)
	            .distinct() // Remove duplicates
	            .collect(Collectors.toList());
	    
	    return assignedFaculty;
	}

//	@Override
//	public void updateFaculty(String facultyCode, Faculty faculty) throws FacultyCollectionException {
//		Optional<Faculty> facultyOpt = facultyRepo.findByFacultyCode(facultyCode);
//        if (facultyOpt.isPresent()) {
//        	Faculty existingFaculty = facultyOpt.get();
//        	existingFaculty.setFacultyName(faculty.getFacultyName());
//        	existingFaculty.setUpdatedAt  (new Date(System.currentTimeMillis()));
//            facultyRepo.save(existingFaculty);
//        } else {
//        	throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(facultyCode));
//        }
//
//	}
	
	@Override
	public void updateFaculty(String fid, Faculty faculty) throws FacultyCollectionException {
		
		Optional<Faculty> facultyOptId = facultyRepo.findById(fid);
		  //Check1 if the facultyID code available in the course
		if (facultyOptId.isPresent()) {
			Faculty facultyUpdate= facultyOptId.get();
	    Optional<Course> course = courseRepo.findByCourseCode(faculty.getCourseCode());
		  //Check2 if the course code available in the course
	    if (!course.isPresent()) {
	        throw new ConstraintViolationException("Course with code " + faculty.getCourseCode() + " not found", null);
	    }
	    //Check3 if the user name available in the user
	    Optional<User> userOpt = userRepo.findByUsername(faculty.getFacultyuserName());
	    if (!userOpt.isPresent()) {
	        throw new ConstraintViolationException("User " + faculty.getFacultyuserName() + " not found", null);
	    } 	
	    User user = userOpt.get();
	    // Check4 if the user's role match the Faculty
	    boolean isFaculty = user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_FACULTY);
	    if (!isFaculty) {
	        throw new FacultyCollectionException("User " + faculty.getFacultyuserName() + " does not have the role of faculty");
	    }
	    // Check5 if the combination of course code and faculty username already exists
	    boolean isDuplicate = facultyRepo.existsByCourseCodeAndFacultyuserName(faculty.getCourseCode(), faculty.getFacultyuserName());
	    if (isDuplicate) {
	        throw new FacultyCollectionException("Course " + faculty.getCourseCode() + " already assigned to " + faculty.getFacultyuserName());
	    }
        	facultyUpdate.setCourseCode(faculty.getCourseCode());
        	facultyUpdate.setFacultyuserName(faculty.getFacultyuserName());
        	facultyUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
        	facultyRepo.save(facultyUpdate);
        } else {
        	throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(fid));
            
        }  
	}

	@Override
	public void deleteByFacultyCode(String fid) throws FacultyCollectionException {
		Optional<Faculty> facultyOpt = facultyRepo.findById(fid);
		
		if(!facultyOpt.isPresent()) {
			throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(fid));
		}else {
			facultyRepo.deleteById(fid);
		}

	}

}
