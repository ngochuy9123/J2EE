package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.PostRepository;
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
    private final PostRepository postRepository;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,PostRepository postRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }
    @Override
    public User save(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName()
                ,userDTO.getEmail(),passwordEncoder.encode( userDTO.getPassword()),userDTO.getPhone(),"User",timestamp,timestamp);
        user.setAvatar(userDTO.getAvatar());
        return userRepository.save(user);
    }

    @Override
    public User getInfo(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }


    @Override
    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean createPost(PostDTO postDTO,String email) {
        Post post = new Post(postDTO.getContent(),postDTO.getVisible(),timestamp,timestamp);
        User user = this.getInfo(email);
        post.setUser(user);
        post.setImageUrl(postDTO.getImageUrl());
        postRepository.save(post);
        return null;
    }



}
