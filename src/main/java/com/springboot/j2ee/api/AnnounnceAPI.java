package com.springboot.j2ee.api;

import com.springboot.j2ee.service.AnnounceService;
import com.springboot.j2ee.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnounnceAPI {
    private final AnnounceService announceService;





    private final UserService userService;


    public AnnounnceAPI(AnnounceService announceService, UserService userService) {
        this.announceService = announceService;
        this.userService = userService;
    }

    @PostMapping("/changeStatusAnnounce")
    @ResponseBody
    public void changeStatusAnnounce(@RequestParam(name = "userId") long userId) {
        announceService.getAnnounceByIdUser(userId);
        announceService.changeToStatusSeen(userId);
    }

}
