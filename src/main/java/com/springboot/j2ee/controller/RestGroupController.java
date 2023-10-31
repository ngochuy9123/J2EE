package com.springboot.j2ee.controller;


import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.GenericResponse;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.ERoomType;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@RestController
public class RestGroupController {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @PostMapping("/api/room")
    public ResponseEntity<GenericResponse> addRoom(@AuthenticationPrincipal CustomUser user, @RequestParam("user[]") Long[] userIdList) {

        var status = roomService.addRoom(user.getUser(), userIdList);
        if (status) {
            return new ResponseEntity<>(
                    new GenericResponse(HttpStatus.OK, "Added Successfully"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    new GenericResponse(HttpStatus.BAD_REQUEST, "Input Invalid"), HttpStatus.BAD_REQUEST);
        }
    }

}
