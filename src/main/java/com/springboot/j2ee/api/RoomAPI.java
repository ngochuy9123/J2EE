package com.springboot.j2ee.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.j2ee.beans.CDCBeans;
import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.GenericResponse;
import com.springboot.j2ee.dto.RoomDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class RoomAPI {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    @Qualifier("cdcBeans")
    private CDCBeans cdcBeans;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/api/room")
    @ResponseBody
    public ResponseEntity<String> addRoom(@AuthenticationPrincipal CustomUser user, @RequestParam(name = "users") String users) throws JsonProcessingException {

        var others = objectMapper.readValue(users, new TypeReference<List<Long>>() {
        });
        
        var status = roomService.addRoom(user.getUser(), others);
//        var status = true;
        return status
                ? new ResponseEntity<>(
                "Added Successfully", HttpStatus.OK)
                : new ResponseEntity<>(
                "Input Invalid", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/room/{id}")
    public ResponseEntity<String> changeUserInRoom(@PathVariable Long id, @RequestParam(name = "users") String users) throws JsonProcessingException {

        var others = objectMapper.readValue(users, new TypeReference<List<Long>>() {
        });

        var status = roomService.changeUser(id, others);
//        var status = true;
        if (status) {
            return new ResponseEntity<>(
                    "Added Successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    "Input Invalid", HttpStatus.BAD_REQUEST);
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
        if (status) {
            cdcBeans.invokeRoomWithType(roomId, "RENAME");
        }

        return status ? new ResponseEntity<>("Xóa thành công", HttpStatus.OK)
                : new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/roomAvatar")
    @ResponseBody
    public ResponseEntity<String> setRoomImage(@RequestParam(name = "roomId") Long roomId, @RequestParam(name = "value") MultipartFile value) {
        var status = roomService.setRoomAvatar(roomId, value);
        if (status) {
            cdcBeans.invokeRoomWithType(roomId, "PHOTO");
        }

        return status ? new ResponseEntity<>("Chỉnh thành công", HttpStatus.OK)
                : new ResponseEntity<>("Chỉnh thất bại", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/isRoomOwner/{roomId}")
    public ResponseEntity<Boolean> getIsRoomOwner(@AuthenticationPrincipal CustomUser user, @PathVariable Long roomId) {
        Room room = roomService.findRoomById(roomId);
        if (Objects.equals(room.getCreatedBy().getId(), user.getUser().getId())) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }

    @GetMapping("/api/roomInfo/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long roomId) {
        Room room = roomService.findRoomById(roomId);
        if (room == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new RoomDTO(room), HttpStatus.OK);
    }

    @GetMapping("/api/findRooms/{name}")
    public ResponseEntity<List<RoomDTO>> findRoom(@AuthenticationPrincipal CustomUser user, @PathVariable String name) {
        var rooms = roomService.findRoomByName(name);
        if (rooms == null) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }

        var roomsWithUser = rooms.stream().filter(r -> r.getParticipants().stream().
                anyMatch(p -> Objects.equals(p.getId(), user.getUser().getId())));
        var roomDtos = roomsWithUser.map(RoomDTO::new).toList();
        return new ResponseEntity<>(roomDtos, HttpStatus.OK);
    }

    @GetMapping("/api/findRooms/")
    public ResponseEntity<List<RoomDTO>> getAllRoom(Principal principal)
    {
        var name = principal.getName();
        var rooms = roomService.getAllRoomByUserName(name).stream().map(RoomDTO::new).toList();

        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

}
