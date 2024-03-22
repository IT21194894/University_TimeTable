package com.timetable.universityTimetable.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Faculty;

@Repository
public interface FacultyRepository extends MongoRepository<Faculty, String> {

	@Query("{'facultyuserName': ?0}")
    Optional<Faculty> findByFacultyuserName(String facultyUserName);
	
    boolean existsByCourseCodeAndFacultyuserName(String courseCode, String facultyUserName);

    @Query("{'courseCode': ?0}")
	List<Faculty> findByCourseCode(String courseCode);
	
    @Query("{'facultyUserName': ?0}")
	List<Faculty>findByFacuserName(String facultyuserName);
}
