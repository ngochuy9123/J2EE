package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User save(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName()
                ,userDTO.getEmail(),passwordEncoder.encode( userDTO.getPassword()),userDTO.getPhone(),"User",timestamp,timestamp);
        return userRepository.save(user);
    }

    @Override
    public User getInfo(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
