package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageDTO {
    private Long id;

    private String message;

    private Long userId;
    private String userName = "";

    private Long roomId;

    private Timestamp timeSend;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.message = message.getMessage();
        this.userId = message.getUser().getId();
        this.userName = message.getUser().getEmail();
        this.roomId = message.getRoom().getId();
        this.timeSend = message.getTimeSend();
    }

    public MessageDTO(String message, Long userId, Long roomId, Timestamp timeSend) {
        this.message = message;
        this.userId = userId;
        this.roomId = roomId;
        this.timeSend = timeSend;
    }
}
