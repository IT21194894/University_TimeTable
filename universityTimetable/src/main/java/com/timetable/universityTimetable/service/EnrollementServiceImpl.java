package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Enrollment;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.EnrollmentRepository;
import com.timetable.universityTimetable.security.service.UserDetailsImpl;

import jakarta.validation.ConstraintViolationException;

@Service
public class EnrollementServiceImpl implements EnrollmentService {

	@Autowired
    public EnrollmentRepository enrollmentRepo;
	
	@Autowired
    public CourseRepository courseRepo;
	
	@Override
    public void createEnrollment(Enrollment enrollment) throws ConstraintViolationException, UniTimetableCollectionException {
		UserDetailsImpl userDetails = getAuthenticatedUser();
	    if (!isStudent(userDetails)) {
	        throw new UniTimetableCollectionException("Only students are allowed to enroll.");
	    }
	    Optional<Course> course = courseRepo.findByCourseCode(enrollment.getCourseCode());
	   
	    if (!course.isPresent()) {
	        throw new ConstraintViolationException("Course with code " + enrollment.getCourseCode() + " not found", null);
	    }
	    boolean enrollOpt = enrollmentRepo.existsByStudentIdAndCourseId(userDetails.getId(),enrollment.getCourseCode());
	    if (!enrollOpt) {
	        enrollment.setStudId(userDetails.getId());
	        enrollment.setCreatedAt(new Date(System.currentTimeMillis()));
	        enrollmentRepo.save(enrollment);
	    } else{
	        throw new UniTimetableCollectionException(UniTimetableCollectionException.EnrollmentAlreadyExist());
	    }
    }

	@Override
    public List<Enrollment> getAllEnrollments() {
        UserDetailsImpl userDetails = getAuthenticatedUser();

        // Check if the user is a student
        if (isStudent(userDetails)) {
            return enrollmentRepo.findByStudId(userDetails.getId());
        } else if (isAdmin(userDetails)){
            return enrollmentRepo.findAll();
        }else {
        	return new ArrayList<>();
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
	private UserDetailsImpl getAuthenticatedUser() {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if (principal instanceof UserDetails) {
	        return (UserDetailsImpl) principal;
	    } else {
	        throw new UsernameNotFoundException("User not authenticated");
	    }
	}
//	@Override
//	public void createEnrollment(Enrollment enrollment) throws ConstraintViolationException, UniTimetableCollectionException {
//		//UserDetailsImpl userDetails = getAuthenticatedUser();
//		Optional<Course> course = courseRepo.findByCourseCode(enrollment.getCourseCode());
//		Optional<Enrollment> enrollOpt = enrollmentRepo.findById(enrollment.getStudId());
//	    if (!course.isPresent()) {
//	        throw new ConstraintViolationException("Course with code " + enrollment.getCourseCode() + " not found", null);
//	    }
//       if(!(course.isPresent()&&enrollOpt.isPresent())){
//		enrollment.setCreatedAt(new Date(System.currentTimeMillis()));
//	            enrollmentRepo.save(enrollment);
//       }
//       else {
//    	   throw new UniTimetableCollectionException(UniTimetableCollectionException.EnrollmentAlreadyExist());
//       }
//
//	}

	@Override
	public Enrollment getEnrollment(String enrollid) throws UniTimetableCollectionException {
		 Optional<Enrollment> enrollmentOpt = enrollmentRepo.findById(enrollid);
	        if (!enrollmentOpt.isPresent()) {
	            throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(enrollid));
	        } else {
	            return enrollmentOpt.get();
	        }
	}

//	@Override
//	public void updateEnrollment(String enrollid, Enrollment enrollment) throws UniTimetableCollectionException {
//		Optional<Enrollment> enrollmentOpt = enrollmentRepo.findById(enrollid);
//        if (enrollmentOpt.isPresent()) {
//            Enrollment updatedEnrollment = enrollmentOpt.get();
//         // Check if the course with the given courseCode exists
//            Optional<Course> course = courseRepo.findByCourseCode(enrollment.getCourseCode());
//            if (!course.isPresent()) {
//                throw new UniTimetableCollectionException("Course with code " + enrollment.getCourseCode() + " not found");
//            }
//            updatedEnrollment.setCourseCode(enrollment.getCourseCode()); // Replace "Field1" with the actual field name to be updated
//            updatedEnrollment.setStudId(enrollment.getStudId()); // Replace "Field2" with the actual field name to be updated
//            // Repeat the above for all fields that need to be updated
//            updatedEnrollment.setUpdatedAt(new Date(System.currentTimeMillis()));
//            enrollmentRepo.save(updatedEnrollment);
//        } else {
//            throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(enrollid));
//        }
//
//	}

	@Override
	public void deleteEnrollment(String enrollid) throws UniTimetableCollectionException {
		Optional<Enrollment> enrollmentOpt = enrollmentRepo.findById(enrollid);
        if (enrollmentOpt.isPresent()) {
            enrollmentRepo.deleteById(enrollid);
        } else {
            throw new UniTimetableCollectionException(UniTimetableCollectionException.NotFoundException(enrollid));
        }

	}
	
	

}
