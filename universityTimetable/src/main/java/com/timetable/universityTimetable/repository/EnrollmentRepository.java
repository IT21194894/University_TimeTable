package com.timetable.universityTimetable.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Enrollment;


@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
	@Query("{'studId': ?0}, 'courseCode':?1")
	boolean existsByStudentIdAndCourseId(String studId, String courseCode);
	
	@Query("{'studId': ?0}")
	List<Enrollment> existingCourses(String studId);

	List<Enrollment> findByStudId(String studentId);

}
