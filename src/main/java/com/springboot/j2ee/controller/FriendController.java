package com.springboot.j2ee.controller;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.entity.Announce;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.AnnounceService;
import com.springboot.j2ee.service.FriendService;
import com.springboot.j2ee.service.UserInfoService;
import com.springboot.j2ee.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    private final AnnounceService announceService;
    private final UserInfoService userInfoService;

    public FriendController(FriendService friendService, UserService userService, AnnounceService announceService, UserInfoService userInfoService) {
        this.friendService = friendService;
        this.userService = userService;
        this.announceService = announceService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("friend")
    public String showFriends(@AuthenticationPrincipal CustomUser principal, Model model){
        User user = userService.getInfoById(principal.getUser().getId());

        List<Friend> lstFriend = friendService.displayListFriend(principal.getUser().getId());
        List<Friend> list_friend_request = friendService.displayFriendRequest(principal.getUser().getId());
        List<Announce> lstAnnounce = announceService.getAnnounceByIdUser(principal.getUser().getId());


        model.addAttribute("infoUser", userInfoService.getInfoByIdUser(user));
        model.addAttribute("lstAnnounce",lstAnnounce);
        model.addAttribute("lst_friend_request",list_friend_request);
        model.addAttribute("idCurrentUser",principal.getUser().getId());
        model.addAttribute("lstFriend",lstFriend);
        model.addAttribute("user",userService.getUserById(principal.getUser().getId()));
        return "friends";
    }

//    @MessageMapping("/socket_addFriend")
//    public Friend sendAddFriendRequest(@Payload Friend request, long idUserFrom){
//
//    }
}
