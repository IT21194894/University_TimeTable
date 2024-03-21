//package com.timetable.universityTimetable.service;
//
//import java.lang.module.ResolutionException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.timetable.universityTimetable.exception.UserCollectionException;
//import com.timetable.universityTimetable.modelclass.User;
//import com.timetable.universityTimetable.repository.UserRepository;
//
//import jakarta.validation.ConstraintViolationException;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    public UserRepository userRepo;
//
//    @Override
//    public void createUser(User user) throws ConstraintViolationException, UserCollectionException {
//        Optional<User> userOptional = userRepo.findByNic(user.getNic());
//        if (userOptional.isPresent()) {
//            throw new UserCollectionException(UserCollectionException.UserAlreadyExist());
//        } else {
//            user.setCreatedAt(new Date(System.currentTimeMillis()));
//            userRepo.save(user);
//        }
//    }
//
//	@Override
//	public List<User> getAllUsers() {
//		List<User> users= userRepo.findAll();
//		if(users.size()>0) {
//			return users;
//		}else {
//			return new ArrayList<User>();
//		}
//	}
//
//	@Override
//	public User getUser(String id) throws UserCollectionException {
//		Optional<User> userOpt = userRepo.findById(id);
//		if(!userOpt.isPresent()) {
//			throw new UserCollectionException(UserCollectionException.NotFoundException(id));
//		}else {
//			return userOpt.get();
//		}
//	}
//
//	@Override
//	public void updateUser(String id, User user) throws UserCollectionException {
//		Optional<User> userOptId = userRepo.findById(id);
//        if (userOptId.isPresent()) {
//        	//if(userOptNic.isPresent()&& !userOptNic.get().getId().equals(id)) {
//        		throw new UserCollectionException(UserCollectionException.UserAlreadyExist());
//        	}
//        	
//            User userUpdate= userOptId.get();
//            userUpdate.setUsername(user.getUsername());
//            userUpdate.setEmail(user.getEmail());
//            userUpdate.setPassword(user.getPassword());
//            userRepo.save(userUpdate);
//        } else {
//        	throw new UserCollectionException(UserCollectionException.NotFoundException(id));
//            
//            
//        }
//		
//	}
//
//	@Override
//	public void deleteUserById(String id) throws UserCollectionException {
//		Optional<User> userOpt = userRepo.findById(id);
//		
//		if(!userOpt.isPresent()) {
//			throw new UserCollectionException(UserCollectionException.NotFoundException(id));
//		}else {
//			userRepo.deleteById(id);
//		}
//		
//	}
//    
//    
//}
