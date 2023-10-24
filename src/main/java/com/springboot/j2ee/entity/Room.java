package com.springboot.j2ee.entity;


import com.springboot.j2ee.enums.ERoomType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;

@Entity
@Data
public class Room {
    @Id
    private Long id;

    private String name;

    private ERoomType roomType;

    @OneToMany
    private ArrayList<User> participants;

    @OneToMany
    private ArrayList<Message> messages;

    private Timestamp lastUpdated;
}
