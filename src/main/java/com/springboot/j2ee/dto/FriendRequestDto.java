package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@AllArgsConstructor
public final class FriendRequestDto {
    private Long userId;
    private String avatar;
    private String email;
    private Timestamp createdAt;


    public FriendRequestDto(Friend friend) {
        userId = friend.getUserFrom().getId();
        avatar = friend.getUserFrom().getAvatar();
        email = friend.getUserFrom().getEmail();
        createdAt = friend.getCreatedAt();
    }


}
