package com.springboot.j2ee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Message {
    @Id
    private Long id;

    private String message;

    @OneToOne
    private User user;

    private Timestamp timeSend;
}
