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
@Document(collection="Timetable")
public class Timetable {
	@Id
	private String ttid;
	
	@NotNull(message= "Course cannot be null")
	private String courseCode;
	

	//private String day;
	
//	@NotNull(message= "Start Time cannot be null")
//	private String startTime;
//	
//	@NotNull(message= "End Time cannot be null")
//	private String endTime;
	
//	@NotNull(message= "Faculty cannot be null")
//	private String facultyCode;
	
//	@NotNull(message= "ClassCode cannot be null")
//	private String classCode;
	
	@NotNull(message= "Booking Id cannot be null")
	private String bookingCode;
	
	@CreatedDate
	private Date createdAt;
	
	@LastModifiedDate
	private Date updatedAt;
	
	public String getTtid() {
        return ttid;
    }

    public void setTtid(String ttid) {
        this.ttid = ttid;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
//    public String getStartTime() {
//        return startTime;
//    }
//    
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
//    
//    public String getEndTime() {
//        return endTime;
//    }
//    
//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }
//    public String getFacultyCode() {
//        return facultyCode;
//    }
//    
//    public void setFacultyCode(String facultyCode) {
//        this.facultyCode = facultyCode;
//    }
    
//    public String getClassCode() {
//        return classCode;
//    }
//    
//    public void setClassCode(String classCode) {
//        this.classCode = classCode;
//    }
    
    public String getBookingCode() {
        return bookingCode;
    }
    
    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
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
