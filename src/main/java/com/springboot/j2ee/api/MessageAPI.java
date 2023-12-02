package com.springboot.j2ee.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.j2ee.dto.MessageDTO;
import com.springboot.j2ee.service.MessageService;
import com.springboot.j2ee.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class MessageAPI {
    @Autowired
    MessageService messageService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/api/roomMessage/{id}")
    public ResponseEntity<List<MessageDTO>> getAllMessageInRoom(@PathVariable Long id) {
        var messages = messageService.getMessagesByRoomId(id);
        var messsageDto = messages.stream().map(MessageDTO::new).toList();

        return new ResponseEntity<>(messsageDto, HttpStatus.OK);
    }

    @PostMapping("/api/message")
    public ResponseEntity<Boolean> addMessage(@RequestBody MessageDTO messageDTO) {
        messageService.save(messageDTO);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/api/photoMessage")
    @ResponseBody
    public ResponseEntity<Boolean> addPhotoMessage(@RequestParam(name = "message") String message, @RequestParam(name = "value") MultipartFile value) throws IOException {

        var messageDTO = objectMapper.readValue(message, MessageDTO.class);


        var uuid = UUID.randomUUID();
        var location = fileUtils.saveFileWithCustomName(value, uuid.toString(), "uploads", "messages", messageDTO.getRoomId().toString(), messageDTO.getUserId().toString());

        messageDTO.setMessage(location);
        messageService.save(messageDTO);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
