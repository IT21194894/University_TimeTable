package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Enrollment;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

}
