package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Faculty;

@Repository
public interface FacultyRepository extends MongoRepository<Faculty, String> {

	@Query("{'facultyCode': ?0}")
    Optional<Faculty> findByFacultyCode(String string);
	
	void deleteByFacultyCode(String facultyCode);
}
