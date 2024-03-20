package com.timetable.universityTimetable.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timetable.universityTimetable.modelclass.User;
import com.timetable.universityTimetable.repository.UserRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	  @Autowired
	  UserRepository userRepository;

	  @Override
	  @Transactional
	  public UserDetails loadUserByUsername(String nic) throws UsernameNotFoundException {
	    User user = userRepository.findByNic(nic)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + nic));

	    return UserDetailsImpl.build(user);
	  }


}
