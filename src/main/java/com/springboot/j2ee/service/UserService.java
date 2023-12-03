package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;

import java.util.List;

public interface UserService {
    User save(UserDTO userDTO);
    User getInfo(String email);

    User getUserById(Long id);

    Boolean checkEmail(String email);
    Boolean createPost(PostDTO postDTO,String email);
    User saveUser(User user);

    User getInfoById(long id);
    User editAvatar(String image,long id);
    User editBackground(String image);
    List<UserDTO> searchUser(String email,Long id);

    Boolean checkOTP(String email,String otp);

    User forgetPass(String email);

}
