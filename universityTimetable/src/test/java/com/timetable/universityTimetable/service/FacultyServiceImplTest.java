package com.timetable.universityTimetable.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.ERole;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.modelclass.Role;
import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.repository.FacultyRepository;
import com.timetable.universityTimetable.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;

public class FacultyServiceImplTest {
	@Mock
    private FacultyRepository facultyRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFacultySuccess() {
        // Mock Course Repository
        Course course = new Course();
        course.setCourseCode("CSE101");
        when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.of(course));

        // Mock User Repository
        User user = new User();
        user.setUsername("john_doe");
        user.setRoles(new HashSet<>(Arrays.asList(new Role(ERole.ROLE_FACULTY))));
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Mock Faculty Repository
        when(facultyRepository.existsByCourseCodeAndFacultyuserName("CSE101", "john_doe")).thenReturn(false);

        // Create Faculty
        Faculty faculty = new Faculty();
        faculty.setCourseCode("CSE101");
        faculty.setFacultyuserName("john_doe");

        try {
            facultyService.createFaculty(faculty);
            // Verify if the faculty is saved
            verify(facultyRepository, times(1)).save(faculty);
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }


    @Test
    public void testCreateFacultyCourseNotFound() {
        // Mock Course Repository
        when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.empty());

        // Create Faculty
        Faculty faculty = new Faculty();
        faculty.setCourseCode("CSE101");
        faculty.setFacultyuserName("john_doe");

        try {
            facultyService.createFaculty(faculty);
            fail("Expected ConstraintViolationException was not thrown");
        } catch (ConstraintViolationException e) {
            assertEquals("Course with code CSE101 not found", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testCreateFaculty_UserNotFound() {
        // Mocking behavior for user not found
        when(courseRepository.findByCourseCode("SE250")).thenReturn(Optional.of(new Course()));
        when(userRepository.findByUsername("faculty1")).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws the expected exception
        assertThrows(ConstraintViolationException.class, () -> {
            Faculty faculty = new Faculty();
            faculty.setCourseCode("SE250");
            faculty.setFacultyuserName("faculty1");
            facultyService.createFaculty(faculty);
        });
    }

    @Test
    void testCreateFaculty_FacultyAlreadyExists() {
        // Mocking behavior for faculty already exists
        when(courseRepository.findByCourseCode("C001")).thenReturn(Optional.of(new Course()));
        when(userRepository.findByUsername("faculty1")).thenReturn(Optional.of(new User()));
        when(facultyRepository.existsByCourseCodeAndFacultyuserName("C001", "faculty1")).thenReturn(true);

        // Call the method under test and assert that it throws the expected exception
        assertThrows(FacultyCollectionException.class, () -> {
            Faculty faculty = new Faculty();
            faculty.setCourseCode("C001");
            faculty.setFacultyuserName("faculty1");
            facultyService.createFaculty(faculty);
        });
    }

    @Test
    void testGetFaculties_EmptyList() {
        // Mocking behavior to return an empty list of faculties
        when(facultyRepository.findAll()).thenReturn(new ArrayList<>());

        // Call the method under test
        List<Faculty> faculties = facultyService.getFaculties();

        // Assert that the returned list is empty
        assertTrue(faculties.isEmpty());
    }
    
    
    @Test
    void testDeleteByFaculty_Success() throws FacultyCollectionException {
        Mockito.when(facultyRepository.findById("F01")).thenReturn(Optional.of(new Faculty()));

        Assertions.assertDoesNotThrow(() -> facultyRepository.deleteById("F01"));
    }

    @Test
    void testDeleteByFaculty_Failure_FacultyIdNotFound() {
        Mockito.when(facultyRepository.findById("F02")).thenReturn(Optional.empty());

        Assertions.assertThrows(FacultyCollectionException.class,
                () -> facultyService.deleteByFacultyCode("F02"));
    }

    
    @Test
    void testGetFaculty_Success() throws FacultyCollectionException {
        // Mocking behavior for finding a faculty by ID
        when(facultyRepository.findById("1")).thenReturn(Optional.of(new Faculty()));

        // Call the method under test
        Faculty faculty = facultyService.getFaculty("1");

        // Assert that the returned faculty is not null
        assertNotNull(faculty);
    }

    @Test
    void testGetFaculty_FacultyNotFound() {
        // Mocking behavior for not finding a faculty by ID
        when(facultyRepository.findById("1")).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws the expected exception
        assertThrows(FacultyCollectionException.class, () -> facultyService.getFaculty("1"));
    }
    
    @Test
    public void testUpdateFacultySuccess() {
        // Mock Faculty Repository
        Faculty existingFaculty = new Faculty();
        existingFaculty.setFid("facultyId");
        existingFaculty.setCourseCode("CSE101");
        existingFaculty.setFacultyuserName("john_doe");
        when(facultyRepository.findById("facultyId")).thenReturn(Optional.of(existingFaculty));

        // Mock Course Repository
        when(courseRepository.findByCourseCode("CSE102")).thenReturn(Optional.of(new Course()));

        // Mock User Repository
        User user = new User();
        user.setUsername("john_doe");
        user.setRoles(new HashSet<>(Arrays.asList(new Role(ERole.ROLE_FACULTY))));
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Create Faculty to update
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setCourseCode("CSE102");
        updatedFaculty.setFacultyuserName("john_doe");

        try {
            facultyService.updateFaculty("facultyId", updatedFaculty);
            verify(facultyRepository, times(1)).save(existingFaculty);
            assertEquals("CSE102", existingFaculty.getCourseCode());
            assertEquals("john_doe", existingFaculty.getFacultyuserName());
            assertNotNull(existingFaculty.getUpdatedAt());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    
    @Test
    void testUpdateFaculty_Failure_NotFound() {
        // Mocking behavior for faculty not found
        when(facultyRepository.findById("fid")).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws the expected exception
        assertThrows(FacultyCollectionException.class, () -> {
            Faculty faculty = new Faculty();
            faculty.setCourseCode("courseCode");
            faculty.setFacultyuserName("facultyUserName");
            facultyService.updateFaculty("fid", faculty);
        });
    }
    
    
    @Test
    public void testUpdateFacultyCourseNotFound() {
        // Mock Faculty Repository
        when(facultyRepository.findById("facultyId")).thenReturn(Optional.of(new Faculty()));

        // Mock Course Repository
        when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.empty());

        // Create Faculty to update
        Faculty faculty = new Faculty();
        faculty.setCourseCode("CSE101");
        faculty.setFacultyuserName("john_doe");

        try {
            facultyService.updateFaculty("facultyId", faculty);
            fail("Expected ConstraintViolationException was not thrown");
        } catch (ConstraintViolationException e) {
            assertEquals("Course with code CSE101 not found", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    
    @Test
    public void testUpdateFaculty_Failure_FacultyUserNameNotFound() throws FacultyCollectionException {
        // Mock behavior for user repository to return empty optional when findByUsername is called
        when(userRepository.findByUsername("facultyUserName")).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws the expected exception
        assertThrows(FacultyCollectionException.class, () -> {
            Faculty faculty = new Faculty();
            faculty.setCourseCode("courseCode");
            faculty.setFacultyuserName("facultyUserName");
            facultyService.updateFaculty("fid", faculty);
        });
    }

}
