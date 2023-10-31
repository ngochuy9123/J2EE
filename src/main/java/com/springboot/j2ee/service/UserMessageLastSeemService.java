package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.UserMessageLastSeen;

import java.util.Optional;

public interface UserMessageLastSeemService {
    Integer updateLastSeen(String email, Long roomId);

    Optional<UserMessageLastSeen> getByEmailAndRoomId(String email, Long roomId);
}
