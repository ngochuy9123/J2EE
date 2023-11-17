package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.UserInfoDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.entity.UserInfo;

public interface UserInfoService {
    UserInfoDTO getInfoByIdUser(User user);
    void updateInfoUser(UserInfoDTO userInfoDTO);
    void addInfoUser(User user);


}
