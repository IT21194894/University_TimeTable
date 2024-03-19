package com.timetable.universityTimetable.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timetable.universityTimetable.exception.CourseCollectionException;
import com.timetable.universityTimetable.exception.FacultyCollectionException;
import com.timetable.universityTimetable.modelclass.Course;
import com.timetable.universityTimetable.modelclass.Faculty;
import com.timetable.universityTimetable.repository.FacultyRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class FacultyServiceImpl implements FacultyService {

	@Autowired
	public FacultyRepository facultyRepo;
	 
	@Override
	public void createFaculty(Faculty faculty)throws ConstraintViolationException, FacultyCollectionException{
		Optional<Faculty> facultyOptional = facultyRepo.findByFacultyCode(faculty.getFacultyCode());
        if (facultyOptional.isPresent()) {
            throw new FacultyCollectionException(FacultyCollectionException.facultyAlreadyExist());
        } else {
        	faculty.setCreatedAt(new Date(System.currentTimeMillis()));
        	facultyRepo.save(faculty);
        }

	}

	@Override
	public List<Faculty> getFaculties() {
		List<Faculty> faculties= facultyRepo.findAll();
		if(faculties.size()>0) {
			return faculties;
		}else {
			return new ArrayList<Faculty>();
		}
	}

	@Override
	public Faculty getFaculty(String facultyCode) throws FacultyCollectionException {
		Optional<Faculty> facultyOpt = facultyRepo.findByFacultyCode(facultyCode);
		if(!facultyOpt.isPresent()) {
			throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(facultyCode));
		}else {
			return facultyOpt.get();
		}
	}

//	@Override
//	public void updateFaculty(String facultyCode, Faculty faculty) throws FacultyCollectionException {
//		Optional<Faculty> facultyOpt = facultyRepo.findByFacultyCode(facultyCode);
//        if (facultyOpt.isPresent()) {
//        	Faculty existingFaculty = facultyOpt.get();
//        	existingFaculty.setFacultyName(faculty.getFacultyName());
//        	existingFaculty.setUpdatedAt  (new Date(System.currentTimeMillis()));
//            facultyRepo.save(existingFaculty);
//        } else {
//        	throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(facultyCode));
//        }
//
//	}
	
	@Override
	public void updateFaculty(String fid, Faculty faculty) throws FacultyCollectionException {
		
		Optional<Faculty> facultyOptId = facultyRepo.findById(fid);
		Optional<Faculty> facultyOptCode = facultyRepo.findByFacultyCode(faculty.getFacultyCode());
		if (facultyOptId.isPresent()) {
        	if(facultyOptCode.isPresent()&& !facultyOptCode.get().getFid().equals(fid)) {
        		throw new FacultyCollectionException(FacultyCollectionException.facultyAlreadyExist());
        	}
        	
        	Faculty facultyUpdate= facultyOptId.get();
            
        	facultyUpdate.setFacultyName(faculty.getFacultyName());
        	facultyUpdate.setFacultyCode(faculty.getFacultyCode());
        	facultyUpdate.setUpdatedAt  (new Date(System.currentTimeMillis()));
        	facultyRepo.save(facultyUpdate);
        } else {
        	throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(fid));
            
        }  
        }

	@Override
	public void deleteByFacultyCode(String facultyCode) throws FacultyCollectionException {
		Optional<Faculty> facultyOpt = facultyRepo.findByFacultyCode(facultyCode);
		
		if(!facultyOpt.isPresent()) {
			throw new FacultyCollectionException(FacultyCollectionException.NotFoundException(facultyCode));
		}else {
			facultyRepo.deleteByFacultyCode(facultyCode);
		}

	}

}
