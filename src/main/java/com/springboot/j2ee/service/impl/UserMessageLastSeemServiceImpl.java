package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.entity.UserMessageLastSeen;
import com.springboot.j2ee.repository.UserMessageLastSeenRepository;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserMessageLastSeemService;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserMessageLastSeemServiceImpl implements UserMessageLastSeemService {
    @Autowired
    private UserMessageLastSeenRepository userMessageLastSeenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Override
    public Integer updateLastSeen(String email, Long roomId) {
        var user = userService.getInfo(email);
        var room = roomService.findRoomById(roomId);

        var now = new Timestamp(System.currentTimeMillis());

        var userMessage = userMessageLastSeenRepository.findByUserAndRoom(user, room);
        UserMessageLastSeen userMessageLastSeen;
        if (userMessage.isEmpty()) {
            userMessageLastSeen = new UserMessageLastSeen(user, room, now);
        }
        else {
            userMessageLastSeen = userMessage.get();
            userMessageLastSeen.setLastSeen(now);
        }

        userMessageLastSeenRepository.save(userMessageLastSeen);

        return 1;

    }

    @Override
    public Optional<UserMessageLastSeen> getByEmailAndRoomId(String email, Long roomId) {
        var user = userService.getInfo(email);
        var room = roomService.findRoomById(roomId);

        return userMessageLastSeenRepository.findByUserAndRoom(user, room);
    }


}
