package com.timetable.universityTimetable.service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Enrollment;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.Timetable;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.repository.ClassRoomRepository;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.EnrollmentRepository;
import com.timetable.universityTimetable.repository.FacultyRepository;
import com.timetable.universityTimetable.repository.TimeTableRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class TimeTableServiceImpl implements TimeTableService {
	
	@Autowired
	public TimeTableRepository timeTableRepo;
	@Autowired
    private CourseRepository courseRepo;
    @Autowired
    private ClassRoomRepository classroomRepo;
    
    @Autowired
    private BookingRepository bookingRepo;
    
    @Autowired
    private FacultyRepository facRepo;
    
    @Autowired
    private EnrollmentRepository enrollRepo;

	@Override
	public void createTimeTable(Timetable timeTable) throws ConstraintViolationException, TimeTableCollectionException {
		 Optional<Timetable> existingTimetable = timeTableRepo.findById(timeTable.getTtid());
	        Optional<Course> existingCourse = courseRepo.findByCourseCode(timeTable.getCourseCode());
	        Optional<Booking> existingBooking = bookingRepo.findByBookId(timeTable.getBookingCode());
	        
	        if (existingTimetable.isPresent()) {
	            throw new TimeTableCollectionException(TimeTableCollectionException.TimeTableAlreadyExist());
	        } else {
	            if (existingCourse.isPresent()) {
	            	
	            	if (existingBooking.isPresent()) {
	            		timeTable.setCreatedAt(new Date(System.currentTimeMillis()));
	            		timeTableRepo.save(timeTable);
	            	}else {
	            		throw new TimeTableCollectionException("No existing booking found" );
	            	}
	            } else {
	            
	                throw new TimeTableCollectionException("Course not found with code: " + timeTable.getCourseCode());
	            }
	        }
	    }


	@Override
	public void updateTimetable(String ttid, Timetable timetable) throws TimeTableCollectionException{
		Optional<Timetable> timeTablesOptId = timeTableRepo.findById(ttid);
        Optional<Course> existingCourse = courseRepo.findByCourseCode(timetable.getCourseCode());
        Optional<Booking> existingBooking = bookingRepo.findByBookId(timetable.getBookingCode());

        if (timeTablesOptId.isPresent()) {
        	
        	Timetable timetableUpdate= timeTablesOptId.get();
        	if (existingCourse.isPresent()) {
//        	timetableUpdate.setStartTime(timetable.getStartTime());
//        	timetableUpdate.setEndTime(timetable.getEndTime());
//        	timetableUpdate.setFacultyCode(timetable.getFacultyCode());
        	timetableUpdate.setCourseCode(timetable.getCourseCode());
        	//timetableUpdate.setClassCode(timetable.getClassCode());
        	if(existingBooking.isPresent()) {
        	timetableUpdate.setBookingCode(timetable.getBookingCode());
        	}else {
        		throw new TimeTableCollectionException("Bookingcode: " + timetable.getCourseCode()+" not found with code");
        	}
//        	timetableUpdate.setPassword(timetable.get());
        	timetableUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
        	timeTableRepo.save(timetableUpdate);
        	 }else {
                 
                 throw new TimeTableCollectionException("Course not found with code: " + timetable.getCourseCode());
             }
        } else {
        	throw new TimeTableCollectionException(TimeTableCollectionException.NotFoundException(ttid));
            
            
        }
	}

	@Override
	public void deleteByTtid(String ttid) throws TimeTableCollectionException {
		Optional<Timetable> timetableOpt = timeTableRepo.findById(ttid);
		
		if(!timetableOpt.isPresent()) {
			throw new TimeTableCollectionException(TimeTableCollectionException.NotFoundException(ttid));
		}else {
			timeTableRepo.deleteById(ttid);
		}

	}

	@Override
	public Map<String, Object> getTimetableWithBookingDetails(String ttid) throws TimeTableCollectionException {
	    Optional<Timetable> timeOpt = timeTableRepo.findById(ttid);
	    if (!timeOpt.isPresent()) {
	        throw new TimeTableCollectionException(TimeTableCollectionException.NotFoundException(ttid));
	    }
	    
	    Timetable timetable = timeOpt.get();
	    String bookCode = timetable.getBookingCode();
	    
	    Optional<Booking> bookingOpt = bookingRepo.findByBookId(bookCode);
	    if (!bookingOpt.isPresent()) {
	        throw new TimeTableCollectionException("Booking not found for bookCode: " + bookCode);
	    }
	    
	    Booking booking = bookingOpt.get();
	    
	    Map<String, Object> result = new HashMap<>();
	    result.put("timetable", timetable);
	    result.put("classCode", booking.getClassCode());
	    result.put("startTime", booking.getStartTime());
	    result.put("endTime", booking.getEndTime());
	    result.put("day", booking.getDay());
	    result.put("bookedBy", booking.getBookedBy());
	    
	    return result;
	}


	@Override
	public List<Map<String, Object>> getTimetableswithBooking() {
		List<Timetable> timetables= timeTableRepo.findAll();
	    List<Map<String, Object>> timetableDetailsList = new ArrayList<>();
	    for (Timetable timetable : timetables) {
	        String bookCode = timetable.getBookingCode();
	        Optional<Booking> bookingOpt = bookingRepo.findByBookId(bookCode);
	        
	        Map<String, Object> timetableDetails = new HashMap<>();
	        timetableDetails.put("timetable", timetable);
	        
	        if (bookingOpt.isPresent()) {
	            Booking booking = bookingOpt.get();
	            timetableDetails.put("classCode", booking.getClassCode());
	            timetableDetails.put("startTime", booking.getStartTime());
	            timetableDetails.put("endTime", booking.getEndTime());
	            timetableDetails.put("day", booking.getDay());
	            timetableDetails.put("bookedBy", booking.getBookedBy());
	        }
	        
	        timetableDetailsList.add(timetableDetails);
	    }
	    
	    return timetableDetailsList;
	}
	
	
	@Override
	public List<Map<String, Object>> getStudentTimetables(String studentId) {
	    // Fetch enrolled courses for the student from the enrollment repository
	    List<Enrollment> enrollments = enrollRepo.findByStudId(studentId);
	    
	    // Initialize a list to store timetable and booking details for enrolled courses
	    List<Map<String, Object>> studentTimetables = new ArrayList<>();
	    
	    // Iterate through the enrolled courses
	    for (Enrollment enrollment : enrollments) {
	        // Fetch timetables for each enrolled course from the timetable repository
	        List<Timetable> courseTimetables = timeTableRepo.findByTimetableCourseId(enrollment.getCourseCode());
	        
	        // Iterate through the timetables for the course
	        for (Timetable timetable : courseTimetables) {
	            // Get the booking details for the timetable
	            Map<String, Object> timetableDetails = new HashMap<>();
	            timetableDetails.put("timetable", timetable);
	            
	            // Find the booking details for the timetable
	            Optional<Booking> bookingOpt = bookingRepo.findByBookId(timetable.getBookingCode());
	            if (bookingOpt.isPresent()) {
	                Booking booking = bookingOpt.get();
	                timetableDetails.put("classCode", booking.getClassCode());
	                timetableDetails.put("startTime", booking.getStartTime());
	                timetableDetails.put("endTime", booking.getEndTime());
	                timetableDetails.put("day", booking.getDay());
	                timetableDetails.put("bookedBy", booking.getBookedBy());
	            }
	            
	            // Add the timetable and booking details to the list
	            studentTimetables.add(timetableDetails);
	        }
	    }
	    
	    return studentTimetables;
	}


	@Override
	public List<Map<String, Object>> getFacultyTimetables(String facultyUserName) {
	    // Fetch assigned courses for the faculty from the faculty repository
	    List<String> assignedCourseCodes = getAssignedCoursesForUsername(facultyUserName);
	    
	    // Initialize a list to store timetable and booking details for assigned courses
	    List<Map<String, Object>> facultyTimetables = new ArrayList<>();
	    
	    // Iterate through the assigned course codes
	    for (String courseCode : assignedCourseCodes) {
	        // Fetch timetables for each assigned course from the timetable repository
	        List<Timetable> courseTimetables = timeTableRepo.findByTimetableCourseId(courseCode);
	        
	        // Iterate through the timetables for the course
	        for (Timetable timetable : courseTimetables) {
	            // Get the booking details for the timetable
	            Map<String, Object> timetableDetails = new HashMap<>();
	            timetableDetails.put("timetable", timetable);
	            
	            // Find the booking details for the timetable
	            Optional<Booking> bookingOpt = bookingRepo.findByBookId(timetable.getBookingCode());
	            if (bookingOpt.isPresent()) {
	                Booking booking = bookingOpt.get();
	                timetableDetails.put("classCode", booking.getClassCode());
	                timetableDetails.put("startTime", booking.getStartTime());
	                timetableDetails.put("endTime", booking.getEndTime());
	                timetableDetails.put("day", booking.getDay());
	                timetableDetails.put("bookedBy", booking.getBookedBy());
	            }
	            
	            // Add the timetable and booking details to the list
	            facultyTimetables.add(timetableDetails);
	        }
	    }
	    
	    return facultyTimetables;
	}

	
	public List<String> getAssignedCoursesForUsername(String facultyUserName) {
	    // Query the Faculty repository to find all entries with the given facultyUserName
	    Optional<Faculty> facultyOpt = facRepo.findByFacultyuserName(facultyUserName);
	    
	    // Check if faculty entry exists for the given username
	    if (facultyOpt.isPresent()) {
	        // Extract the course codes from the faculty entry
	    	 String courseCode = facultyOpt.get().getCourseCode();
	        
	    	 return Collections.singletonList(courseCode);
	    } else {
	        // If no faculty entry found for the username, return an empty list
	        return Collections.emptyList();
	    }
	}

	

}
