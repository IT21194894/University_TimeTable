package com.timetable.universityTimetable.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Timetable;

@Repository
public interface TimeTableRepository extends MongoRepository<Timetable, String> {

	
	@Query("{'ttid': ?0}")
	Optional<Timetable> findByTimetableId(String ttid);
	
	@Query("{'courseCode': ?0}")
	List<Timetable> findByTimetableCourseId(String courseCode);
	


}
