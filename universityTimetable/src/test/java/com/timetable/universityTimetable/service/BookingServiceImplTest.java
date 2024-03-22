package com.timetable.universityTimetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.repository.ClassRoomRepository;

public class BookingServiceImplTest {
	@Mock
    private BookingRepository bookingRepository;

    @Mock
    private ClassRoomRepository classRoomRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBookings() {
        // Given
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        bookings.add(booking1);
        bookings.add(booking2);
        when(bookingRepository.findAll()).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getAllBookings();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetBooking() throws UniTimetableCollectionException {
        // Given
        String bookId = "1";
        Booking booking = new Booking();
        booking.setBookid(bookId);
        when(bookingRepository.findById(bookId)).thenReturn(Optional.of(booking));

        // When
        Booking result = bookingService.getBooking(bookId);

        // Then
        assertNotNull(result);
        assertEquals(bookId, result.getBookid());
    }
    
    @Test
    public void testUpdateBooking_ClassroomNotAvailable() {
        // Given
        String bookId = "1";
        Booking existingBooking = new Booking();
        existingBooking.setClassCode("ExistingClassCode");
        when(bookingRepository.findById(bookId)).thenReturn(Optional.of(existingBooking));

        Booking updatedBooking = new Booking();
        updatedBooking.setClassCode("NewClassCode");

        // When/Then
        assertThrows(UniTimetableCollectionException.class, () -> bookingService.updateBooking(bookId, updatedBooking));
        verify(bookingRepository, never()).save(any(Booking.class));
    }
    
    @Test
    void testDeleteByCourseCode_Success() throws CourseCollectionException {
        Mockito.when(bookingRepository.findByBookId("1")).thenReturn(Optional.of(new Booking()));


        Assertions.assertDoesNotThrow(() -> bookingService.deleteBooking("CSE101"));
    }

    @Test
    void testDeleteByCourseCode_Failure_CourseNotFound() {
        Mockito.when(bookingRepository.findByBookId("CSE101")).thenReturn(Optional.empty());

        Assertions.assertThrows(CourseCollectionException.class,
                () -> bookingService.deleteBooking("CSE101"));
    }
}
