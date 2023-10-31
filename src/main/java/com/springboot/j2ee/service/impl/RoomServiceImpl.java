package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.ERoomType;
import com.springboot.j2ee.repository.RoomRepository;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserService userService;

    @Override
    public List<Room> getAllRoomByUserName(String name) {
        User user = userService.getInfo(name);
        return roomRepository.findRoomsByParticipantsContainsOrderByLastUpdated(user);
    }

    @Override
    public Room addNewRoom() {
        return null;
    }

    @Override
    public Room findRoomById(Long id) {
        return roomRepository.findRoomById(id);
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public boolean addRoom(User mainUser, Long[] others) {
        if (others == null || others.length == 0) {
            return false;
        }

        Room room = new Room();

        ERoomType roomType;
        if (others.length == 1) {
            roomType = ERoomType.NORMAL;
        }
        else {
            roomType = ERoomType.GROUP;
        }
        room.setRoomType(roomType);
        room.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        List<User> users = Arrays.stream(others).map(id -> userService.getUserById(id)).toList();
        room.setParticipants(users);


        var names = users.stream().map(User::getFirstName)
                .reduce("", (s, s2) -> s +", "+ s2);


        room.setName(names);

        save(room);

        return true;
    }
}
