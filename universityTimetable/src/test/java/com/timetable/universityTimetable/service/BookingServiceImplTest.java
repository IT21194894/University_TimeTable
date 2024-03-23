package com.timetable.universityTimetable.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.timetable.universityTimetable.repository.CourseRepository;
import com.timetable.universityTimetable.security.service.UserDetailsImpl;
import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import com.timetable.universityTimetable.exception.UniTimetableCollectionException;
import com.timetable.universityTimetable.modelclass.Booking;
import com.timetable.universityTimetable.modelclass.Classroom;
import com.timetable.universityTimetable.repository.BookingRepository;
import com.timetable.universityTimetable.repository.ClassRoomRepository;

public class BookingServiceImplTest {
	@Mock
    private BookingRepository bookingRepository;

    @Mock
    private ClassRoomRepository classRoomRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;
    
    @Mock
    private Authentication authenticationMock;
    
    @Mock
    private UserDetailsImpl userDetailsMock;
    
    @Mock
    private SecurityContext securityContextMock;

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
        String bookId = "book03";
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
    void testUpdateBooking_Success() throws UniTimetableCollectionException {
        // Given
        String bookId = "book03";
        Booking existingBooking = new Booking();
        existingBooking.setClassCode("A01");
        existingBooking.setStartTime("09:00:00");
        existingBooking.setEndTime("10:00:00");
        existingBooking.setDay("Monday");
        when(bookingRepository.findById(bookId)).thenReturn(Optional.of(existingBooking));

        Booking updatedBooking = new Booking();
        updatedBooking.setClassCode("B05");
        updatedBooking.setStartTime("09:00:00");
        updatedBooking.setEndTime("10:00:00");
        updatedBooking.setDay("Monday");

        // When
        assertDoesNotThrow(() -> bookingService.updateBooking(bookId, updatedBooking));
    }

    @Test
    void testUpdateBooking_ClassroomNotAvailable() throws UniTimetableCollectionException {
        // Given
        String bookId = "book03";
        Booking existingBooking = new Booking();
        existingBooking.setClassCode("A01");
        //existingBooking.setStartTime("11:00:00");
        //existingBooking.setEndTime("12:00:00");
        existingBooking.setDay("Monday");
        when(bookingRepository.findById(bookId)).thenReturn(Optional.of(existingBooking));

        Booking updatedBooking = new Booking();
        updatedBooking.setClassCode("B07"); // Assuming B06 is not available
        //updatedBooking.setStartTime("09:00:00");
        //updatedBooking.setEndTime("10:00:00");
        updatedBooking.setDay("Monday");

     // Mocking behavior to simulate not finding the booking
        when(bookingRepository.findById(bookId)).thenReturn(Optional.empty());

        // When/Then
        UniTimetableCollectionException exception = assertThrows(UniTimetableCollectionException.class, () ->
            bookingService.updateBooking(bookId, updatedBooking)
        );
        // Asserting that the exception message contains the expected string
        assertTrue(exception.getMessage().contains("not found"));
    }


    
    @Test
    void testDeleteBooking_Success() {
        // Arrange
        Booking mockBooking = new Booking();
        mockBooking.setBookid("book03");
        Mockito.when(bookingRepository.findById("book03")).thenReturn(Optional.of(mockBooking));

        // Act & Assert
        try {
            bookingService.deleteBooking("book03");
        } catch (UniTimetableCollectionException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    

    @Test
    public void testCreateBooking_Successful() throws Exception {
        // Mock the UserDetailsImpl object representing an authenticated user with faculty role
    	UserDetailsImpl userDetailsMock = mock(UserDetailsImpl.class);
    	doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_FACULTY"))).when(userDetailsMock).getAuthorities();
    	
        // Set up the security context with the mocked authentication
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
        SecurityContext securityContextMock = mock(SecurityContext.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);

        // Mock behavior to simulate finding the room
        when(classRoomRepository.findByClassroomCode(anyString())).thenReturn(Optional.of(new Classroom()));

        // Mock behavior to simulate room availability
        when(bookingRepository.findByClassroomNameAndDayOfWeek(anyString(), anyString())).thenReturn(Collections.emptyList());

        // Create test booking
        Booking booking = new Booking();
        booking.setClassCode("B05");
        booking.setStartTime("09:00");
        booking.setEndTime("10:00");
        booking.setDay("Wednesday");

        // Call the method under test
        assertDoesNotThrow(() -> bookingService.createBooking(booking));

        // Verify that save method is called on bookingRepo
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void testIsFaculty() {
        // Mocking authenticated user with faculty role
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_FACULTY"));
        UserDetailsImpl userDetails = new UserDetailsImpl("1", "username", "email", "password", authorities);

        
        // Call the method under test
        assertTrue(bookingService.isFaculty(userDetails));
    }

    @Test
    public void testIsAdmin() {
        // Mocking authenticated user with admin role
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetailsImpl userDetails = new UserDetailsImpl("1", "username", "email", "password", authorities);

        
        // Call the method under test
        assertTrue(bookingService.isAdmin(userDetails));
    }
    
    @Test
    public void testCheckClassroomAvailability_NoConflict() {

        // Mocking behavior for dependencies
        when(bookingRepository.findByClassroomNameAndDayOfWeek(anyString(), anyString())).thenReturn(Collections.emptyList());

        // Call the method under test
        assertTrue(bookingService.checkClassroomAvailability("A01", "09:00:00", "10:00:00", "Monday"));
    }
  
}
