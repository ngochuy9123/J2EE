package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    List<Room> getAllRoomByUserName(String name);

    Room addNewRoom();

    Room findRoomById(Long id);

    void save(Room room);

    boolean addRoom(User mainUser, List<Long> others);

    boolean removeUserFromRoom(Long userId, Long roomId);

    boolean renameRoom(Long roomId, String value);

    boolean setRoomAvatar(Long roomId, MultipartFile file);
}
