package com.springboot.j2ee.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserMessageLastSeenId implements Serializable {
    private User user;

    private Room room;
}
