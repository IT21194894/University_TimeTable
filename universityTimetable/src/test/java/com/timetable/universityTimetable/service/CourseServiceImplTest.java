package com.timetable.universityTimetable.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.modelclass.Course;

public class CourseServiceImplTest {
	

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCreateCourse_Success() {
        Course course = new Course();
        course.setCourseCode("CSE101");
        course.setCourseName("Computer Science");
        course.setCredit("3");
        course.setDescription("Introduction to Computer Science");

        when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.empty());
        when(courseRepository.save(any())).thenReturn(course);

        Assertions.assertDoesNotThrow(() -> courseService.createCourse(course));
    }
    @Test
    void testCreateCourse_Failure_AlreadyExists() {
        Course course = new Course();
        course.setCourseCode("CSE101");
        course.setCourseName("Computer Science");
        course.setCredit("3");
        course.setDescription("Introduction to Computer Science");

        Mockito.when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.of(course));

        Assertions.assertThrows(CourseCollectionException.class, () -> courseService.createCourse(course));
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAllCourses();
        Assertions.assertEquals(courses.size(), result.size());
    }

    @Test
    void testGetCourse_Success() throws CourseCollectionException {
        Course course = new Course();
        course.setCourseCode("CSE101");
        course.setCourseName("Computer Science");
        course.setCredit("3");
        course.setDescription("Introduction to Computer Science");

        Mockito.when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.of(course));

        Course result = courseService.getCourse("CSE101");
        Assertions.assertEquals(course.getCourseCode(), result.getCourseCode());
    }

    @Test
    void testGetCourse_Failure_NotFound() {
        Mockito.when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.empty());

        Assertions.assertThrows(CourseCollectionException.class, () -> courseService.getCourse("CSE101"));
    }

    @Test
    void testUpdateCourse_Success() throws CourseCollectionException {
        Course course = new Course();
        course.setCourseCode("CSE101");
        course.setCourseName("Computer Science");
        course.setCredit("3");
        course.setDescription("Introduction to Computer Science");

        Mockito.when(courseRepository.findById("1")).thenReturn(Optional.of(course));
        Mockito.when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.empty());
        Mockito.when(courseRepository.save(Mockito.any())).thenReturn(course);

        Assertions.assertDoesNotThrow(() -> courseService.updateCourse("1", course));
    }

    @Test
    void testUpdateCourse_Failure_CourseNotFound() {
        Mockito.when(courseRepository.findById("1")).thenReturn(Optional.empty());

        Assertions.assertThrows(CourseCollectionException.class,
                () -> courseService.updateCourse("1", new Course()));
    }

    @Test
    void testDeleteByCourseCode_Success() throws CourseCollectionException {
        Mockito.when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.of(new Course()));

        Assertions.assertDoesNotThrow(() -> courseService.deleteByCourseCode("CSE101"));
    }

    @Test
    void testDeleteByCourseCode_Failure_CourseNotFound() {
        Mockito.when(courseRepository.findByCourseCode("CSE101")).thenReturn(Optional.empty());

        Assertions.assertThrows(CourseCollectionException.class,
                () -> courseService.deleteByCourseCode("CSE101"));
    }


}
