package com.springboot.j2ee.controller;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.MessageDTO;
import com.springboot.j2ee.service.MessageService;
import com.springboot.j2ee.service.UserMessageLastSeemService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class MessageController {


    @Autowired
    MessageService messageService;

    @Autowired
    UserMessageLastSeemService userMessageLastSeemService;

    @PostMapping("/message")
    public String addMessage(@RequestBody MessageDTO messageDTO) {
        messageService.save(messageDTO);
        return "A";
    }



    @GetMapping("/api/room/missed/{id}")
    public ResponseEntity<Integer> getAllMissedMessage(@AuthenticationPrincipal CustomUser user, @PathVariable Long id) {
        var userMessageLastSeen = userMessageLastSeemService.getByEmailAndRoomId(user.getUsername(), id);

        if (userMessageLastSeen.isEmpty()) {
            lifelineUpdate(user, id);
            userMessageLastSeen = userMessageLastSeemService.getByEmailAndRoomId(user.getUsername(), id);

//            return getAllMissedMessage(user, id);
        }

        var total = messageService.getMissedMessage(id, userMessageLastSeen.get().getLastSeen());

        return new ResponseEntity<>(total, HttpStatus.OK);

    }

    @GetMapping("/api/message/lifeline/{roomId}")
    public ResponseEntity<Integer> lifelineUpdate(@AuthenticationPrincipal CustomUser user, @PathVariable Long roomId) {

        if (roomId == null) {
            return new ResponseEntity<Integer>(-2, HttpStatus.BAD_REQUEST);
        }

        if (roomId == 0) {
            return new ResponseEntity<Integer>(0, HttpStatus.OK);
        }

        var email = user.getUsername();

        var status = userMessageLastSeemService.updateLastSeen(email, roomId);

        return new ResponseEntity<Integer>(status, HttpStatus.OK);

    }

}
