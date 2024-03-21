package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Classroom;


@Repository
public interface ClassRoomRepository extends MongoRepository<Classroom, String>{
	@Query("{'classroomCode': ?0}")
    Optional<Classroom> findByClassroomCode(String classroomCode);
	
	void deleteByClassroomCode(String classroomCode);
}
