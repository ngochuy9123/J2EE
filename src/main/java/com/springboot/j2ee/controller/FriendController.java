package com.springboot.j2ee.controller;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.service.FriendService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("friend")
    public String showFriends(@AuthenticationPrincipal CustomUser principal, Model model){
        List<Friend> lstFriend = friendService.displayListFriend(principal.getUser().getId());
        model.addAttribute("idCurrentUser",principal.getUser().getId());
        model.addAttribute("lstFriend",lstFriend);
        return "friends";
    }

//    @MessageMapping("/socket_addFriend")
//    public Friend sendAddFriendRequest(@Payload Friend request, long idUserFrom){
//
//    }
}
