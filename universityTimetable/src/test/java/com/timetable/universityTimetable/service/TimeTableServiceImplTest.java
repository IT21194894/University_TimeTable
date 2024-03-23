package com.timetable.universityTimetable.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.anyString;

import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.exception.TimeTableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Enrollment;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.Timetable;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.EnrollmentRepository;
import com.timetable.universityTimetable.repository.FacultyRepository;
import com.timetable.universityTimetable.repository.TimeTableRepository;


public class TimeTableServiceImplTest {
	 @Mock
	    private TimeTableRepository timeTableRepo;

	    @Mock
	    private CourseRepository courseRepo;

	    @Mock
	    private BookingRepository bookingRepo;
	    
	    @Mock
	    private EnrollmentRepository enrollmentRepo;
	    
	    @Mock
	    private FacultyRepository facRepo;

	    @InjectMocks
	    private TimeTableServiceImpl timeTableService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }

	    @Test
	    public void testCreateTimeTable_Success() {
	        Timetable timeTable = new Timetable();
	        timeTable.setTtid("timetableId");
	        timeTable.setCourseCode("CSE101");
	        timeTable.setBookingCode("bookingId");

	        when(timeTableRepo.findById("timetableId")).thenReturn(Optional.empty());
	        when(courseRepo.findByCourseCode("CSE101")).thenReturn(Optional.of(new Course()));
	        when(bookingRepo.findByBookId("bookingId")).thenReturn(Optional.of(new Booking()));

	        assertDoesNotThrow(() -> timeTableService.createTimeTable(timeTable));
	    }

	    @Test
	    public void testCreateTimeTable_TimeTableAlreadyExists() {
	    	 Timetable existingTimetable = new Timetable();
	    	    existingTimetable.setTtid("timetableId");

	    	    when(timeTableRepo.findById("timetableId")).thenReturn(Optional.of(existingTimetable));

	    	    Timetable newTimetable = new Timetable();
	    	    newTimetable.setTtid("timetableId");

	        assertThrows(TimeTableCollectionException.class,
	                () -> timeTableService.createTimeTable(newTimetable));
	    }
	    
	    
	    @Test
	    void testDeleteTimeTable_Success() throws TimeTableCollectionException {
	        Mockito.when(timeTableRepo.findById("T01")).thenReturn(Optional.of(new Timetable()));

	        Assertions.assertDoesNotThrow(() -> timeTableRepo.deleteById("T01"));
	    }

	    @Test
	    void testDeleteByFaculty_Failure_FacultyIdNotFound() {
	        Mockito.when(timeTableRepo.findById("T23")).thenReturn(Optional.empty());

	        Assertions.assertThrows(TimeTableCollectionException.class,
	                () -> timeTableService.deleteByTtid("T23"));
	    }
	    
	    @Test
	    public void testUpdateTimetable_Success() {
	        // Mock existing timetable
	        Timetable existingTimetable = new Timetable();
	        existingTimetable.setTtid("timetableId");
	        existingTimetable.setCourseCode("existingCourseCode");
	        existingTimetable.setBookingCode("existingBookingCode");

	        when(timeTableRepo.findById("timetableId")).thenReturn(Optional.of(existingTimetable));

	        // Mock existing course
	        when(courseRepo.findByCourseCode("newCourseCode")).thenReturn(Optional.of(new Course()));

	        // Mock existing booking
	        when(bookingRepo.findByBookId("newBookingCode")).thenReturn(Optional.of(new Booking()));

	        // Create a new timetable to update
	        Timetable updatedTimetable = new Timetable();
	        updatedTimetable.setTtid("timetableId");
	        updatedTimetable.setCourseCode("newCourseCode");
	        updatedTimetable.setBookingCode("newBookingCode");

	        assertDoesNotThrow(() -> timeTableService.updateTimetable("timetableId", updatedTimetable));
	    }

	    @Test
	    public void testUpdateTimetable_TimetableNotFound() {
	        // Mock timeTableRepo.findById to return an empty Optional
	        when(timeTableRepo.findById("nonexistentId")).thenReturn(Optional.empty());

	        // Create a new timetable to update
	        Timetable updatedTimetable = new Timetable();
	        updatedTimetable.setTtid("nonexistentId");

	        assertThrows(TimeTableCollectionException.class,
	                () -> timeTableService.updateTimetable("nonexistentId", updatedTimetable));
	       
	    }
	    
	    @Test
	    public void testGetTimetableWithBookingDetails_Success() throws TimeTableCollectionException {
	        String ttid = "timetableId";
	        String bookCode = "bookingId";

	        Timetable timetable = new Timetable();
	        timetable.setTtid(ttid);
	        timetable.setBookingCode(bookCode);

	        Booking booking = new Booking();
	        booking.setBookid(bookCode);
	        booking.setClassCode("class123");
	        booking.setStartTime("09:00");
	        booking.setEndTime("11:00");
	        booking.setDay("Monday");
	        booking.setBookedBy("John Doe");

	        when(timeTableRepo.findById(ttid)).thenReturn(Optional.of(timetable));
	        when(bookingRepo.findByBookId(bookCode)).thenReturn(Optional.of(booking));

	        Map<String, Object> result = timeTableService.getTimetableWithBookingDetails(ttid);

	        assertEquals(timetable, result.get("timetable"));
	        assertEquals("class123", result.get("classCode"));
	        assertEquals("09:00", result.get("startTime"));
	        assertEquals("11:00", result.get("endTime"));
	        assertEquals("Monday", result.get("day"));
	        assertEquals("John Doe", result.get("bookedBy"));
	    }
	    
	    @Test
	    public void testGetTimetableWithBookingDetails_TimeTableNotFound() {
	        String ttid = "timetableId";

	        when(timeTableRepo.findById(ttid)).thenReturn(Optional.empty());

	        assertThrows(TimeTableCollectionException.class,
	                () -> timeTableService.getTimetableWithBookingDetails(ttid));	    
	    }

	    @Test
	    public void testGetTimetableWithBookingDetails_BookingNotFound() {
	        String ttid = "timetableId";
	        String bookCode = "bookingId";

	        Timetable timetable = new Timetable();
	        timetable.setTtid(ttid);
	        timetable.setBookingCode(bookCode);

	        when(timeTableRepo.findById(ttid)).thenReturn(Optional.of(timetable));
	        when(bookingRepo.findByBookId(bookCode)).thenReturn(Optional.empty());

	        TimeTableCollectionException exception = assertThrows(TimeTableCollectionException.class,
	                () -> timeTableService.getTimetableWithBookingDetails(ttid));
	        assertEquals("Booking not found for bookCode: bookingId", exception.getMessage());
	    }
	    
	    @Test
	    public void testGetTimetableswithBooking_Success() {
	        Timetable timetable1 = new Timetable();
	        timetable1.setTtid("timetableId1");
	        timetable1.setBookingCode("bookingId1");

	        Booking booking1 = new Booking();
	        booking1.setBookid("bookingId1");
	        booking1.setClassCode("class123");
	        booking1.setStartTime("09:00");
	        booking1.setEndTime("11:00");
	        booking1.setDay("Monday");
	        booking1.setBookedBy("John Doe");

	        Timetable timetable2 = new Timetable();
	        timetable2.setTtid("timetableId2");
	        timetable2.setBookingCode("bookingId2");

	        Booking booking2 = new Booking();
	        booking2.setBookid("bookingId2");
	        booking2.setClassCode("class456");
	        booking2.setStartTime("14:00");
	        booking2.setEndTime("16:00");
	        booking2.setDay("Wednesday");
	        booking2.setBookedBy("Jane Doe");

	        List<Timetable> timetables = Arrays.asList(timetable1, timetable2);
	        when(timeTableRepo.findAll()).thenReturn(timetables);
	        when(bookingRepo.findByBookId("bookingId1")).thenReturn(Optional.of(booking1));
	        when(bookingRepo.findByBookId("bookingId2")).thenReturn(Optional.of(booking2));

	        List<Map<String, Object>> result = timeTableService.getTimetableswithBooking();

	        assertEquals(2, result.size());

	        Map<String, Object> timetableDetails1 = result.get(0);
	        assertEquals(timetable1, timetableDetails1.get("timetable"));
	        assertEquals("class123", timetableDetails1.get("classCode"));
	        assertEquals("09:00", timetableDetails1.get("startTime"));
	        assertEquals("11:00", timetableDetails1.get("endTime"));
	        assertEquals("Monday", timetableDetails1.get("day"));
	        assertEquals("John Doe", timetableDetails1.get("bookedBy"));

	        Map<String, Object> timetableDetails2 = result.get(1);
	        assertEquals(timetable2, timetableDetails2.get("timetable"));
	        assertEquals("class456", timetableDetails2.get("classCode"));
	        assertEquals("14:00", timetableDetails2.get("startTime"));
	        assertEquals("16:00", timetableDetails2.get("endTime"));
	        assertEquals("Wednesday", timetableDetails2.get("day"));
	        assertEquals("Jane Doe", timetableDetails2.get("bookedBy"));
	    }
	    
	    @Test
	    public void testGetStudentTimetables_Success() {
	        String studentId = "student123";
	        String courseCode = "CSE101";
	        String bookingCode = "booking123";

	        Enrollment enrollment = new Enrollment();
	        enrollment.setStudId(studentId);
	        enrollment.setCourseCode(courseCode);

	        Timetable timetable = new Timetable();
	        timetable.setTtid("timetableId");
	        timetable.setBookingCode(bookingCode);

	        Booking booking = new Booking();
	        booking.setBookid(bookingCode);
	        booking.setClassCode("class123");
	        booking.setStartTime("09:00");
	        booking.setEndTime("11:00");
	        booking.setDay("Monday");
	        booking.setBookedBy("John Doe");

	        List<Enrollment> enrollments = Collections.singletonList(enrollment);
	        when(enrollmentRepo.findByStudId(studentId)).thenReturn(enrollments);
	        when(timeTableRepo.findByTimetableCourseId(courseCode)).thenReturn(Collections.singletonList(timetable));
	        when(bookingRepo.findByBookId(bookingCode)).thenReturn(Optional.of(booking));

	        List<Map<String, Object>> result = timeTableService.getStudentTimetables(studentId);

	        assertEquals(1, result.size());
	        Map<String, Object> timetableDetails = result.get(0);
	        assertEquals(timetable, timetableDetails.get("timetable"));
	        assertEquals("class123", timetableDetails.get("classCode"));
	        assertEquals("09:00", timetableDetails.get("startTime"));
	        assertEquals("11:00", timetableDetails.get("endTime"));
	        assertEquals("Monday", timetableDetails.get("day"));
	        assertEquals("John Doe", timetableDetails.get("bookedBy"));
	    }

	    @Test
	    public void testGetFacultyTimetables_Success() {
	        String facultyUserName = "professor123";
	        String courseCode = "CSE101";
	        String bookingCode = "booking123";

	        List<String> assignedCourseCodes = Collections.singletonList(courseCode);

	        Timetable timetable = new Timetable();
	        timetable.setTtid("timetableId");
	        timetable.setBookingCode(bookingCode);

	        Booking booking = new Booking();
	        booking.setBookid(bookingCode);
	        booking.setClassCode("class123");
	        booking.setStartTime("09:00");
	        booking.setEndTime("11:00");
	        booking.setDay("Monday");
	        booking.setBookedBy("John Doe");

	        Faculty faculty = new Faculty();
	        faculty.setFacultyuserName(facultyUserName);
	        faculty.setCourseCode(courseCode);

	        when(timeTableRepo.findByTimetableCourseId(courseCode)).thenReturn(Collections.singletonList(timetable));
	        when(bookingRepo.findByBookId(bookingCode)).thenReturn(Optional.of(booking));
	        when(facRepo.findByFacultyuserName(facultyUserName)).thenReturn(Optional.of(faculty));

	        List<Map<String, Object>> result = timeTableService.getFacultyTimetables(facultyUserName);

	        assertEquals(1, result.size());
	        Map<String, Object> timetableDetails = result.get(0);
	        assertEquals(timetable, timetableDetails.get("timetable"));
	        assertEquals("class123", timetableDetails.get("classCode"));
	        assertEquals("09:00", timetableDetails.get("startTime"));
	        assertEquals("11:00", timetableDetails.get("endTime"));
	        assertEquals("Monday", timetableDetails.get("day"));
	        assertEquals("John Doe", timetableDetails.get("bookedBy"));
	    }
}


