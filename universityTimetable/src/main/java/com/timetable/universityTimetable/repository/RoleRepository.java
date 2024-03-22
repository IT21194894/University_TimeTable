package com.timetable.universityTimetable.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.timetable.universityTimetable.modelclass.Role;
import com.timetable.universityTimetable.modelclass.ERole;

public interface RoleRepository extends MongoRepository<Role, String> {
	  Optional<Role> findByName(ERole name);
	}