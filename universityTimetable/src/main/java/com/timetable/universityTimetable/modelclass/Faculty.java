package com.timetable.universityTimetable.modelclass;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Faculty")
public class Faculty {
	@Id
	private String fid;
	
	@NotNull(message= "Faculty User Name cannot be null")
	private String facultyuserName;
	
	@NotNull(message= "Course Code cannot be null")
	private String courseCode;
	
	
	@CreatedDate
	private Date createdAt;
	
	@LastModifiedDate
	private Date updatedAt;
	
	//Getters & Setters
	public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
    
    public String getFacultyuserName() {
        return facultyuserName;
    }

    public void setFacultyuserName(String facultyuserName) {
        this.facultyuserName = facultyuserName;
    }
    
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
