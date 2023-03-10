package com.billyclub.points.security.services;

import com.billyclub.points.model.UserEntity;
import com.billyclub.points.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  public UserEntity saveSignupUser(UserDetailsImpl userDetails)  {
    UserEntity user = new UserEntity(null,
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getPassword(),
            null);

    return userRepository.save(user);
  }

}
