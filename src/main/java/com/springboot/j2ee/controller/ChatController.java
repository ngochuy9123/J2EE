package com.springboot.j2ee.controller;

import com.springboot.j2ee.beans.CDCBeans;
import com.springboot.j2ee.config.CDCBeansConfig;
import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.MessageDTO;
import com.springboot.j2ee.dto.RoomDTO;
import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.service.MessageService;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


@Controller
@AllArgsConstructor
public class ChatController {
    private final UserService userService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    @Qualifier("cdcBeans")
    private CDCBeans cdcBeans;


    @Autowired
    private RoomService roomService;

    @Autowired
    private MessageService messageService;

    private static final String messageSocket = "/topic/message/";

    @MessageMapping("/{id}/{uuid}")
    public void receive(Principal principal, String message, @DestinationVariable String id, @DestinationVariable String uuid) throws Exception {
        System.out.println(message);
        simpMessagingTemplate.convertAndSend(messageSocket+id+"/"+uuid, "Hi");

    }
    private void handleMessage(Message message, UUID uuid, CustomUser user) {
        var id = user.getUser().getId();
        var dto = new MessageDTO(message);
        simpMessagingTemplate.convertAndSend(messageSocket+id+"/"+uuid.toString(), dto);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDTO>> getAllRoom(Principal principal)
    {
        var name = principal.getName();
        var rooms = roomService.getAllRoomByUserName(name).stream().map(RoomDTO::new).toList();

        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }



    @GetMapping("/room/{id}")
    public ResponseEntity<List<MessageDTO>> getAllMessageInRoom(@PathVariable Long id) {
        var messages = messageService.getMessagesByRoomId(id);
        var messsageDto = messages.stream().map(MessageDTO::new).toList();

        return new ResponseEntity<>(messsageDto, HttpStatus.OK);
    }

    @PostMapping("/room")
    public void addRoom(Principal principal, @RequestBody String name, @RequestBody String[] userId) {
        var rooms = roomService.getAllRoomByUserName(name);

    }

    @GetMapping("chat")
    public String chat(@AuthenticationPrincipal CustomUser user , Model model){
        var userName = user.getUsername();
        UUID uuid = UUID.randomUUID();
        var id = user.getUser().getId();


        cdcBeans.subscribeToWriteMessage(id, uuid, (message) -> handleMessage(message, uuid, user) );

        System.out.println("Current Logged in User is: " + userName);
        model.addAttribute("user", user);
        model.addAttribute("uuid", uuid);
        return "chat_app";
    }

}
