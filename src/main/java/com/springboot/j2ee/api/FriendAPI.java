package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendAPI {
    private final FriendService friendService;

    public FriendAPI(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("addFriend")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long userToId){
        friendService.sendFriendRequest(UserController.user_pub.getId(), userToId);
        return new ResponseEntity<>("Gui loi moi thanh cong", HttpStatus.CREATED);
    }

}
