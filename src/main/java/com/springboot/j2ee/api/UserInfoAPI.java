package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.dto.UserInfoDTO;
import com.springboot.j2ee.service.UserInfoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserInfoAPI {

    private final UserInfoService userInfoService;

    public UserInfoAPI(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    @PostMapping("showInfoUser")
    @ResponseBody
    public UserInfoDTO searchUser(@AuthenticationPrincipal CustomUser principal){
        return userInfoService.getInfoByIdUser(principal.getUser());
    }


    @PostMapping("editInfoUser")
    @ResponseBody
    public void updateInfoUser(@ModelAttribute("user") UserInfoDTO userInfoDTO,@AuthenticationPrincipal CustomUser principal){
        userInfoDTO.setIdUser(principal.getUser().getId());
        userInfoService.updateInfoUser(userInfoDTO);
    }


}
