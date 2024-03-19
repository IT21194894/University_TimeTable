package com.timetable.universityTimetable.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Timetable;

@Repository
public interface TimeTableRepository extends MongoRepository<Timetable, String> {
	


}
