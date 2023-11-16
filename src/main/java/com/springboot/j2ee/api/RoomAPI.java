package com.springboot.j2ee.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.GenericResponse;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RoomAPI {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/api/room")
    @ResponseBody
    public ResponseEntity<GenericResponse> addRoom(@AuthenticationPrincipal CustomUser user, @RequestParam(name = "users") String users) throws JsonProcessingException {

        var others = objectMapper.readValue(users, new TypeReference<List<Long>>() {
        });
        
        var status = roomService.addRoom(user.getUser(), others);
//        var status = true;
        if (status) {
            return new ResponseEntity<>(
                    new GenericResponse(HttpStatus.OK, "Added Successfully"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    new GenericResponse(HttpStatus.BAD_REQUEST, "Input Invalid"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/room")
    @ResponseBody
    public ResponseEntity<String> removeFromRoom(@AuthenticationPrincipal CustomUser user, @RequestParam(name = "roomId") Long roomId) {
        var status = roomService.removeUserFromRoom(user.getUser().getId(), roomId);
        return status ? new ResponseEntity<>("Xóa thành công", HttpStatus.OK)
                      : new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/api/room")
    @ResponseBody
    public ResponseEntity<String> renameRoom(@RequestParam(name = "roomId") Long roomId, @RequestParam(name = "value") String value) {
        var status = roomService.renameRoom(roomId, value);
        return status ? new ResponseEntity<>("Xóa thành công", HttpStatus.OK)
                : new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/roomAvatar")
    @ResponseBody
    public ResponseEntity<String> setRoomImage(@RequestParam(name = "roomId") Long roomId, @RequestParam(name = "value") MultipartFile value) {
        var status = roomService.setRoomAvatar(roomId, value);
        return status ? new ResponseEntity<>("Xóa thành công", HttpStatus.OK)
                : new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
    }
}
