package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable.universityTimetable.modelclass.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	 @Query("{'nic': ?0}")
	    Optional<User> findByNic(String nic);
	 
	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);
	  
	  Optional <User>findByUsername(String username);

}
