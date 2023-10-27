package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;

public interface UserService {
    User save(UserDTO userDTO);
    User getInfo(String email);
    Boolean checkEmail(String email);
    Boolean createPost(PostDTO postDTO,String email);
    User saveUser(User user);

    User editAvatar(String image);
    User editBackground(String image);
}