package com.timetable.universityTimetable.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.timetable.universityTimetable.UniversityTimetableApplication;
import com.timetable.universityTimetable.config.MongoDBTestContainerConfig;
import com.timetable.universityTimetable.config.TestWebSecurityConfig;
import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.service.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { MongoDBTestContainerConfig.class, TestWebSecurityConfig.class, UniversityTimetableApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseControllerIntegrationTest {
	

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private static final String COURSE_CODE = "CODE";
    private static final String COURSE_NAME = "Name";
    private static final String UPDATED_NAME = "DIFFERENT_NAME";

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseCode(COURSE_CODE);
        testCourse.setCourseName(COURSE_NAME);
    }

    @Test
    void testCreateCourse() throws Exception {
    	Mockito.doNothing().when(courseService).createCourse(Mockito.any(Course.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"code\": \"CODE\", \"courseName\": \"Name\", \"description\": \"description\", \"credit\": 3 }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Course registered successfully"));
    }

    @Test
    void testGetSingleCourse() throws Exception {
        Mockito.when(courseService.getCourse(COURSE_CODE))
                .thenReturn(testCourse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/course/" + COURSE_CODE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(COURSE_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value(COURSE_NAME));
    }

    @Test
    void testUpdateCourseByCode() throws Exception {
        Mockito.doNothing().when(courseService).updateCourse(Mockito.eq(COURSE_CODE), Mockito.any(Course.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/auth/course/" + COURSE_CODE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"code\": \"CODE\", \"courseName\": \"DIFFERENT_NAME\", \"description\": \"description\", \"credit\": 3 }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Course updated successfully"));
    }

    @Test
    void testDeleteCourseByCode() throws Exception {
        Mockito.doNothing().when(courseService).deleteByCourseCode(COURSE_CODE);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/course/" + COURSE_CODE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Course with ID CODE deleted successfully"));
    }
}
