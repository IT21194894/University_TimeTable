package com.timetable.universityTimetable.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import com.timetable.universityTimetable.modelclass.Role;
import com.timetable.universityTimetable.modelclass.ERole;

public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}
