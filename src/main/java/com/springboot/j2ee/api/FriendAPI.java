package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.dto.FriendDTO;
import com.springboot.j2ee.dto.FriendRequestDto;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.FriendService;
import com.springboot.j2ee.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class FriendAPI {
    private final FriendService friendService;
    private final UserService userService;

    public FriendAPI(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @PostMapping("addFriend")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long userToId,@AuthenticationPrincipal CustomUser principal){
        User user = userService.getInfo(principal.getUsername());
        friendService.sendFriendRequest(user.getId(), userToId);
        return new ResponseEntity<>("Gui loi moi thanh cong", HttpStatus.CREATED);
    }

    @PostMapping("acceptFriendRequest")
    public ResponseEntity<String> acceptFriendRequest(@AuthenticationPrincipal CustomUser principal,@RequestParam Long userToId){
        friendService.acceptFriendRequest(principal.getUser().getId(),userToId);
        return ResponseEntity.ok("Da Chap Nhan Loi Moi Ket Ban");
    }

    @PostMapping("declineFriendRequest")
    public ResponseEntity<String> declineFriendRequest(@AuthenticationPrincipal CustomUser principal,@RequestParam Long userToId){
        System.out.println(principal.getUser().getId());
        friendService.declineFriendRequest(principal.getUser().getId(),userToId);

        return ResponseEntity.ok("Da Tu Choi Loi Moi Ket Ban");
    }

    @GetMapping("/api/friends")
    public ResponseEntity<List<UserDTO>> getUser(@AuthenticationPrincipal CustomUser user) {
        var friends = friendService.displayListFriend(user.getUser().getId());

        List<UserDTO> userDTOS = friends.stream().map(
                f -> !Objects.equals(f.getUserFrom().getId(), user.getUser().getId())
                        ? new UserDTO(f.getUserFrom())
                        : new UserDTO(f.getUserTo())
        ).toList();
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }
    @GetMapping("/api/friendRequests")
    public ResponseEntity<List<FriendRequestDto>> getFriendRequests(@AuthenticationPrincipal CustomUser user) {
        List<Friend> friendRequests = friendService.displayFriendRequest(user.getUser().getId());
        var friendRequestDto = friendRequests.stream().map(FriendRequestDto::new).toList();

        return new ResponseEntity<>(friendRequestDto, HttpStatus.OK);
    }


}
