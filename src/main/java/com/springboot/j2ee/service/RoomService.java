package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;

import java.util.List;

public interface RoomService {
    List<Room> getAllRoomByUserName(String name);

    Room addNewRoom();

    Room findRoomById(Long id);

    void save(Room room);

    boolean addRoom(User mainUser, Long[] others);
}
