package com.timetable.universityTimetable.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
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
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.repository.ClassRoomRepository;
import jakarta.validation.ConstraintViolationException;

public class ClassroomServiceImplTest {
	@Mock
    private ClassRoomRepository classRoomRepository;

    @InjectMocks
    private ClassroomServiceImpl classroomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClassRooms() {
        // Mocking behavior to return a list of classrooms
        List<Classroom> mockClassrooms = new ArrayList<>();
        mockClassrooms.add(new Classroom());
        mockClassrooms.add(new Classroom());
        when(classRoomRepository.findAll()).thenReturn(mockClassrooms);

        // Call the method under test
        List<Classroom> result = classroomService.getAllClassRooms();

        // Verify the result
        assertEquals(2, result.size());
    }

    @Test
    void testGetClassroom_ExistingClassroom() throws UniTimetableCollectionException {
        // Mocking behavior to return an optional containing a classroom
        Classroom mockClassroom = new Classroom();
        mockClassroom.setClassroomCode("ABC123");
        Optional<Classroom> optionalClassroom = Optional.of(mockClassroom);
        when(classRoomRepository.findByClassroomCode("ABC123")).thenReturn(optionalClassroom);

        // Call the method under test
        Classroom result = classroomService.getClassroom("ABC123");

        // Verify the result
        assertNotNull(result);
        assertEquals("ABC123", result.getClassroomCode());
    }

    @Test
    void testGetClassroom_NonExistingClassroom() {
        // Mocking behavior to return an empty optional
        when(classRoomRepository.findByClassroomCode("XYZ987")).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws the expected exception
        assertThrows(UniTimetableCollectionException.class, () -> classroomService.getClassroom("XYZ987"));
    }

    
    @Test
    void testDeleteByClassCode_Failure_ClassNotFound() {
        Mockito.when(classRoomRepository.findByClassroomCode("B07")).thenReturn(Optional.empty());

        Assertions.assertThrows(UniTimetableCollectionException.class,
                () -> classroomService.deleteByClassroomCode("B07"));
    }
    
    @Test
    void testDeleteByClassCode_Success() throws UniTimetableCollectionException {
        Mockito.when(classRoomRepository.findByClassroomCode("B05")).thenReturn(Optional.of(new Classroom()));

        Assertions.assertDoesNotThrow(() -> classroomService.deleteByClassroomCode("B05"));
    }

    
    @Test
    void testUpdateClassroom_ExistingClassroom_Success() throws UniTimetableCollectionException {
        // Mocking behavior to return an optional containing the existing classroom
        Classroom existingClassroom = new Classroom();
        existingClassroom.setCid("1");
        existingClassroom.setClassroomCode("ABC123");
        existingClassroom.setCapacity(30);
        existingClassroom.setProjectorAvailable(true);
        Optional<Classroom> optionalClassroom = Optional.of(existingClassroom);
        when(classRoomRepository.findById("1")).thenReturn(optionalClassroom);
        when(classRoomRepository.findByClassroomCode("ABC123")).thenReturn(optionalClassroom);

        // Mocking behavior to return an empty optional for the classroom with updated code
        Classroom updatedClassroom = new Classroom();
        updatedClassroom.setCid("2");
        updatedClassroom.setClassroomCode("XYZ987");
        when(classRoomRepository.findByClassroomCode("XYZ987")).thenReturn(Optional.empty());

        // Call the method under test
        assertDoesNotThrow(() -> classroomService.updateClassroom("1", updatedClassroom));
    }

    @Test
    void testUpdateClassroom_ClassroomNotFound() {
        // Mocking behavior to return an empty optional
        when(classRoomRepository.findById("1")).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws the expected exception
        assertThrows(UniTimetableCollectionException.class, () -> {
            Classroom updatedClassroom = new Classroom();
            updatedClassroom.setClassroomCode("XYZ987");
            classroomService.updateClassroom("1", updatedClassroom);
        });
    }

    @Test
    void testUpdateClassroom_DuplicateClassroomCode() {
        // Mocking behavior to return an optional containing another classroom with the same code
        Classroom existingClassroom = new Classroom();
        existingClassroom.setCid("2");
        existingClassroom.setClassroomCode("XYZ987");
        Optional<Classroom> optionalClassroom = Optional.of(existingClassroom);
        when(classRoomRepository.findById("1")).thenReturn(Optional.of(new Classroom()));
        when(classRoomRepository.findByClassroomCode("XYZ987")).thenReturn(optionalClassroom);

        // Call the method under test and assert that it throws the expected exception
        assertThrows(UniTimetableCollectionException.class, () -> {
            Classroom updatedClassroom = new Classroom();
            updatedClassroom.setClassroomCode("XYZ987");
            classroomService.updateClassroom("1", updatedClassroom);
        });
    }

    @Test
    void testCreateClassroom_NewClassroom_Success() throws UniTimetableCollectionException {
        // Mocking behavior to return an empty optional
        when(classRoomRepository.findByClassroomCode("ABC123")).thenReturn(Optional.empty());

        // Call the method under test
        assertDoesNotThrow(() -> {
            Classroom newClassroom = new Classroom();
            newClassroom.setClassroomCode("ABC123");
            classroomService.createClassroom(newClassroom);
        });
    }

    @Test
    void testCreateClassroom_DuplicateClassroomCode() {
        // Mocking behavior to return an optional containing an existing classroom with the same code
        Classroom existingClassroom = new Classroom();
        existingClassroom.setClassroomCode("ABC123");
        Optional<Classroom> optionalClassroom = Optional.of(existingClassroom);
        when(classRoomRepository.findByClassroomCode("ABC123")).thenReturn(optionalClassroom);

        // Call the method under test and assert that it throws the expected exception
        assertThrows(UniTimetableCollectionException.class, () -> {
            Classroom newClassroom = new Classroom();
            newClassroom.setClassroomCode("ABC123");
            classroomService.createClassroom(newClassroom);
        });
    }

}
