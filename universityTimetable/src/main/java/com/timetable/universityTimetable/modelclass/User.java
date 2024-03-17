package com.timetable.universityTimetable.modelclass;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="uni_timetable")

public class User {
	@Id
	private String ID;
	private String userName;
	private String password;
	private String role;
	
	
}
