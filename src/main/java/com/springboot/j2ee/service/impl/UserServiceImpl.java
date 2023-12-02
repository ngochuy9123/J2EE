package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.EmailService;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final EmailService emailService;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, PostRepository postRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.emailService = emailService;
    }
    @Override
    public User save(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName()
                ,userDTO.getEmail(),passwordEncoder.encode( userDTO.getPassword()),userDTO.getPhone(),"User",timestamp,timestamp);
        user.setAvatar(userDTO.getAvatar());
        user.setBackground(userDTO.getBackground());
        user.setUsername(userDTO.getUsername());
        user.setOtpConfirm(false);
        generateOTP(user);
        return userRepository.save(user);
    }

    public void generateOTP(User user){
        String otp = taoChuoiNgauNhien(8);
        user.setOtp(otp);
        user.setOtpRequestTime(timestamp);

        emailService.sendSimpleEmail(user.getEmail(),otp);
    }

    public void clearOTP(User user){
        user.setOtp(null);
        user.setOtpRequestTime(null);
        user.setOtpConfirm(true);
        userRepository.save(user);
    }

    @Override
    public Boolean checkOTP(String email, String otp) {
        User user = userRepository.findByEmail(email);
//        kiem tra OTP chinh xac va qua thoi gian hay chua
//        Neu qua thoi gian thi gui lai
        if (user.getOtp().equals(otp)){

            if (user.isOTPRequired()){
                clearOTP(user);
                return true;
            }
            else{
                clearOTP(user);
                String otp_temp = taoChuoiNgauNhien(8);
                user.setOtp(otp_temp);
                user.setOtpRequestTime(timestamp);
                userRepository.save(user);
                emailService.sendSimpleEmail(user.getEmail(),otp);
                return false;
            }

        }


        return false;
    }

    public static String taoChuoiNgauNhien(int doDai) {
        String kyTu = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder chuoiNgauNhien = new StringBuilder();

        // Sử dụng đối tượng Random để chọn ngẫu nhiên ký tự từ chuỗi kyTu
        Random random = new Random();
        for (int i = 0; i < doDai; i++) {
            int viTri = random.nextInt(kyTu.length());
            chuoiNgauNhien.append(kyTu.charAt(viTri));
        }

        return chuoiNgauNhien.toString();
    }

    @Override
    public User getInfo(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
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

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getInfoById(long id) {
        Optional<User> u = userRepository.findById(id);
        return u.get();
    }

    @Override
    public User editAvatar(String image,long id) {

        User user = userRepository.findById(id).get();
        user.setAvatar(image);

        return userRepository.save(user);
    }

    @Override
    public User editBackground(String image) {
        return null;
    }

    @Override
    public List<UserDTO> searchUser(String email,Long id) {
//        return userRepository.findByEmailLikeAndIdNot(email,id);
        var lstUser = userRepository.findByEmailLike(email);
        List<UserDTO> dsUserDTO = new ArrayList<>();
        for (User u:lstUser) {
             dsUserDTO.add(changeToDTO(u));
        }
        return dsUserDTO;
    }



    public UserDTO changeToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setBackground(user.getBackground());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhone(user.getPhone());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        return userDTO;
    }

    @Override
    public List<User> getUserByEmailLimitBy(String email, int limit) {
        return userRepository.findUsersByEmailContainingIgnoreCase(email, PageRequest.of(0, limit));
    }

    @Override
    public List<User> getUserByEmailLimitBy(String email, int start, int count) {
        return userRepository.findUsersByEmailContainingIgnoreCase(email, PageRequest.of(start, start + count));
    }

}
