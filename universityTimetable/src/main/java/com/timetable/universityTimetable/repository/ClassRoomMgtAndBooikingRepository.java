package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.ClassroomMgtAndBooking;


@Repository
public interface ClassRoomMgtAndBooikingRepository extends MongoRepository<ClassroomMgtAndBooking, String>{
	@Query("{'classroomCode': ?0}")
    Optional<ClassroomMgtAndBooking> findByClassroomCode(String classroomCode);
	
	void deleteByClassroomCode(String classroomCode);
}
