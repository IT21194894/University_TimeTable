package com.timetable.universityTimetable.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.User;
@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
