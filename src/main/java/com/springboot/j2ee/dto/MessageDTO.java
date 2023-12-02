package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EMessageType;
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
    private String avatar;
    private EMessageType messageType;

    private Timestamp timeSend;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.message = message.getMessage();
        this.userId = message.getUser().getId();
        this.userName = message.getUser().getEmail();
        this.avatar = message.getUser().getAvatar();
        this.roomId = message.getRoom().getId();
        this.timeSend = message.getTimeSend();
        this.messageType = message.getMessageType();
    }

    public MessageDTO(String message, Long userId, Long roomId, Timestamp timeSend) {
        this.message = message;
        this.userId = userId;
        this.roomId = roomId;
        this.timeSend = timeSend;
    }
}
