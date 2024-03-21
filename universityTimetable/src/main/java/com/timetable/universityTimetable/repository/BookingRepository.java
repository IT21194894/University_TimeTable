package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

	@Query("{'bookid': ?0}")
    Optional<Booking> findByBookId(String bookid);
}
