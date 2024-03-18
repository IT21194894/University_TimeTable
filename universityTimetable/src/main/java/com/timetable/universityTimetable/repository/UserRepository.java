package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	@Query("{'user': ?0}")
	Optional<User> findByUserName(String userName);

}
