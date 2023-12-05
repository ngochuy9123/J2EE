package com.springboot.j2ee.entity;


import com.springboot.j2ee.enums.ERoomType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private ERoomType roomType;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> participants = new ArrayList<>();

    @OneToMany
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    private User createdBy;

    private Timestamp lastUpdated;

    private String roomAvatar;
}
