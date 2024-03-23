package com.timetable.universityTimetable.service;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Enrollment;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.EnrollmentRepository;
import com.timetable.universityTimetable.security.service.UserDetailsImpl;

import jakarta.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class EnrollmentServiceImplTest {
	@Mock
    private CourseRepository courseRepoMock;

    @Mock
    private EnrollmentRepository enrollmentRepoMock;

    @Mock
    private UserDetailsImpl userDetailsMock;

    @InjectMocks
    private EnrollementServiceImpl enrollmentService;

    private static final String COURSE_CODE = "CODE";
    private static final String USER_ID = "123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    void testCreateEnrollmentWithNonExistentCourse() {
        // Prepare test data
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseCode("NON_EXISTENT_COURSE_CODE");
        enrollment.setStudId("USER_ID");

        // Mock the behavior of the dependencies
        when(courseRepoMock.findByCourseCode("NON_EXISTENT_COURSE_CODE")).thenReturn(Optional.empty());

        // Mock the authenticated user as a student
        when(userDetailsMock.getId()).thenReturn("studentId");
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")))
            .when(userDetailsMock).getAuthorities();

        // Mock the SecurityContext and Authentication objects
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Set up the SecurityContext to return the mocked Authentication object
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Authentication object to return the mocked UserDetailsImpl
        when(authentication.getPrincipal()).thenReturn(userDetailsMock);

        // Call the method under test and assert the exception
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> enrollmentService.createEnrollment(enrollment)
        );

        // Verify that the exception message is correct
        assertThat(exception.getMessage(), equalTo("Course with code NON_EXISTENT_COURSE_CODE not found"));

        // Verify that the method was not called to save the enrollment
        verify(enrollmentRepoMock, never()).save(any(Enrollment.class));
    }


    
    @Test
    void testCreateEnrollment_StudentEnrollsInNewCourse_Success() throws Exception {
        // Mock the authenticated user
       
        userDetailsMock.setId("studentId");
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")))
        .when(userDetailsMock).getAuthorities();
        // Mock the SecurityContext and Authentication objects
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Set up the SecurityContext to return the mocked Authentication object
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Authentication object to return the mocked UserDetailsImpl
        when(authentication.getPrincipal()).thenReturn(userDetailsMock);

        // Mock the course repository to return a course with the specified code
        String courseCode = "COURSE001";
        Course course = new Course();
        course.setCourseCode(courseCode);
        when(courseRepoMock.findByCourseCode(courseCode)).thenReturn(Optional.of(course));

        // Mock the enrollment repository to indicate that the student has not already enrolled in this course
        when(enrollmentRepoMock.existsByStudentIdAndCourseId("studentId", courseCode)).thenReturn(false);

        // Prepare the enrollment object
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseCode(courseCode);

        // Call the method under test
        assertDoesNotThrow(() -> enrollmentService.createEnrollment(enrollment));

        // Verify that the enrollment was saved
        verify(enrollmentRepoMock, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testGetAllEnrollments_Student_ReturnsStudentEnrollments() {
        // Mock the authenticated user as a student
        UserDetailsImpl userDetails = new UserDetailsImpl("studentId", "student", "student@example.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));

        // Mock the SecurityContext and Authentication objects
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Set up the SecurityContext to return the mocked Authentication object
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Authentication object to return the mocked UserDetailsImpl
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock the enrollment repository to return student enrollments
        List<Enrollment> studentEnrollments = new ArrayList<>();
        when(enrollmentRepoMock.findByStudId("studentId")).thenReturn(studentEnrollments);

        // Call the method under test
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Verify that the enrollment repository's findByStudId method was called with the correct student ID
        verify(enrollmentRepoMock).findByStudId("studentId");

        // Verify that the result matches the student enrollments
        assertEquals(studentEnrollments, result);
    }

    @Test
    void testGetAllEnrollments_Admin_ReturnsAllEnrollments() {
        // Mock the authenticated user as an admin
        UserDetailsImpl userDetails = new UserDetailsImpl("adminId", "admin", "admin@example.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // Mock the SecurityContext and Authentication objects
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Set up the SecurityContext to return the mocked Authentication object
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Authentication object to return the mocked UserDetailsImpl
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock the enrollment repository to return all enrollments
        List<Enrollment> allEnrollments = new ArrayList<>();
        when(enrollmentRepoMock.findAll()).thenReturn(allEnrollments);

        // Call the method under test
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Verify that the enrollment repository's findAll method was called
        verify(enrollmentRepoMock).findAll();

        // Verify that the result matches all enrollments
        assertEquals(allEnrollments, result);
    }

    @Test
    void testGetAllEnrollments_NonStudentNonAdmin_ReturnsEmptyList() {
        // Mock the authenticated user as a non-student and non-admin
        UserDetailsImpl userDetails = new UserDetailsImpl("userId", "user", "user@example.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_OTHER")));

        // Mock the SecurityContext and Authentication objects
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Set up the SecurityContext to return the mocked Authentication object
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Authentication object to return the mocked UserDetailsImpl
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Call the method under test
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Verify that the enrollment repository was not interacted with
        verifyNoInteractions(enrollmentRepoMock);

        // Verify that the result is an empty list
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    void testGetEnrollment_EnrollmentExists_Success() throws Exception {
        // Mock dependencies
        
        // Prepare test data
        String enrollmentId = "ENROLLMENT_ID";
        Enrollment expectedEnrollment = new Enrollment();
        expectedEnrollment.setEnrollId(enrollmentId);
        Optional<Enrollment> enrollmentOpt = Optional.of(expectedEnrollment);
        
        // Mock behavior of the enrollment repository to return the expected enrollment
        when(enrollmentRepoMock.findById(enrollmentId)).thenReturn(enrollmentOpt);
        
        // Call the method under test
        Enrollment result = enrollmentService.getEnrollment(enrollmentId);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(enrollmentId, result.getEnrollId());
    }
    
    void testGetEnrollment_EnrollmentNotFound_ExceptionThrown() {
        // Mock dependencies
        
        // Prepare test data
        String enrollmentId = "NON_EXISTENT_ENROLLMENT_ID";
        
        // Mock behavior of the enrollment repository to return an empty optional
        when(enrollmentRepoMock.findById(enrollmentId)).thenReturn(Optional.empty());
        
        // Call the method under test and assert that an exception is thrown
        UniTimetableCollectionException exception = assertThrows(UniTimetableCollectionException.class, () -> {
            enrollmentService.getEnrollment(enrollmentId);
        });
        
        // Verify the exception message contains the enrollment ID
        assertTrue(exception.getMessage().contains(enrollmentId));
    }

    @Test
    void testDeleteByEnrollId_Success() throws UniTimetableCollectionException {
        Mockito.when(enrollmentRepoMock.findById("CSE101")).thenReturn(Optional.of(new Enrollment()));

        Assertions.assertDoesNotThrow(() -> enrollmentService.deleteEnrollment("CSE101"));
    }

    @Test
    void testDeleteByCourseCode_Failure_CourseNotFound() {
        Mockito.when(enrollmentRepoMock.findById("CSE101")).thenReturn(Optional.empty());

        Assertions.assertThrows(UniTimetableCollectionException.class,
                () -> enrollmentService.deleteEnrollment("CSE101"));
    }



}
