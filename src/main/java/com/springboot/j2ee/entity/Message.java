package com.springboot.j2ee.entity;

import com.springboot.j2ee.enums.EMessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;

    private EMessageType messageType;

    private Timestamp timeSend;

    public Message(String message, User user, Room room, EMessageType messageType, Timestamp timeSend) {
        this.message = message;
        this.user = user;
        this.room = room;
        this.timeSend = timeSend;
        this.messageType = messageType;
    }


}
