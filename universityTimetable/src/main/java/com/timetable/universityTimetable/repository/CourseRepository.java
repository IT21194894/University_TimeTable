package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

	@Query("{'courseCode': ?0}")
    Optional<Course> findByCourseCode(String courseCode);
	
	void deleteByCourseCode(String courseCode);
}
