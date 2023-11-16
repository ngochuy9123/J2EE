package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.ERoomType;
import com.springboot.j2ee.repository.RoomRepository;
import com.springboot.j2ee.service.RoomService;
import com.springboot.j2ee.service.UserService;
import com.springboot.j2ee.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserService userService;

    @Autowired
    FileUtils fileUtils;

    @Override
    public List<Room> getAllRoomByUserName(String name) {
        User user = userService.getInfo(name);
        return roomRepository.findRoomsByParticipantsContainsOrderByLastUpdatedDesc(user);
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
    public boolean addRoom(User mainUser, List<Long> others) {
        if (others == null || others.isEmpty()) {
            return false;
        }

        Room room = new Room();

        ERoomType roomType;
        if (others.size() == 1) {
            roomType = ERoomType.NORMAL;
        }
        else {
            roomType = ERoomType.GROUP;
        }
        room.setRoomType(roomType);
        room.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        List<User> users = new java.util.ArrayList<>(others.stream().map(id -> userService.getUserById(id)).toList());
        users.add(userService.getUserById(mainUser.getId()));
        room.setParticipants(users);


        var names = String.join(",", users.stream().map(User::getFirstName).toList());

        room.setName(names);

        save(room);

        return true;
    }

    @Override
    public boolean removeUserFromRoom(Long userId, Long roomId) {
        var user = userService.getUserById(userId);
        var room = roomRepository.findRoomById(roomId);
        room.getParticipants().remove(user);
        roomRepository.save(room);
        return true;
    }

    @Override
    public boolean renameRoom(Long roomId, String value) {
        var room = roomRepository.findRoomById(roomId);
        room.setName(value);
        roomRepository.save(room);
        return true;
    }

    @Override
    public boolean setRoomAvatar(Long roomId, MultipartFile file) {
        var room = roomRepository.findRoomById(roomId);
        try {
            var location = fileUtils.saveFile(file, "uploads", "rooms", roomId.toString());
            room.setRoomAvatar(location);
            roomRepository.save(room);

        } catch (IOException e) {
            System.out.println("Cannot save file");
        }


        return true;
    }
}
