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
@Document(collection="classroom")
public class Classroom {
	@Id
    private String classid;

    @NotNull(message = "Classroom Code cannot be null")
    private String classroomCode;

    @NotNull(message = "capacity Code cannot be null")
    private int capacity;
    
    @NotNull(message = "projector availability cannot be null")
    private boolean projectorAvailable;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    // Getters & Setters
    public String getCid() {
        return classid;
    }

    public void setCid(String classid) {
        this.classid = classid;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public void setClassroomCode(String classroomCode) {
        this.classroomCode = classroomCode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isProjectorAvailable() {
        return projectorAvailable;
    }

    public void setProjectorAvailable(boolean projectorAvailable) {
        this.projectorAvailable = projectorAvailable;
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
