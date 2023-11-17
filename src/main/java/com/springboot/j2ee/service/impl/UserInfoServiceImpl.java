package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.UserInfoDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.entity.UserInfo;
import com.springboot.j2ee.repository.UserInfoRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service

public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserRepository userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }



    @Override
    public UserInfoDTO getInfoByIdUser(User user) {
        UserInfo userInfo = userInfoRepository.findByUserInfo(user);

        return changeToDTO(userInfo);
    }

    @Override
    public void updateInfoUser(UserInfoDTO userInfoDTO) {
        UserInfo userInfo = userInfoRepository.findByUserInfo(userRepository.findById(userInfoDTO.getIdUser()).get());

        userInfo.setGithub(userInfoDTO.getGithub());
        userInfo.setTwitter(userInfo.getTwitter());
        userInfo.setLocation(userInfoDTO.getLocation());
        userInfo.setInstagram(userInfoDTO.getInstagram());



        userInfoRepository.save(userInfo);

    }

    @Override
    public void addInfoUser(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(user);
        userInfo.setGithub("Chua co thong tin github");
        userInfo.setTwitter("Chua co thong tin twitter");
        userInfo.setInstagram("Chua co thong tin instagram");
        userInfo.setLocation("Chua co thong tin location");
        userInfoRepository.save(userInfo);
    }


    public UserInfoDTO changeToDTO(UserInfo userInfo){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setGithub(userInfo.getGithub());
        userInfoDTO.setInstagram(userInfo.getInstagram());
        userInfoDTO.setIdUser(userInfoDTO.getIdUser());
        userInfoDTO.setTwitter(userInfo.getTwitter());
        userInfoDTO.setLocation(userInfo.getLocation());
        return userInfoDTO;
    }


}
