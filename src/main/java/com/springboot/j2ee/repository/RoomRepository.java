package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    ArrayList<Room> findRoomsByParticipantsContainsOrderByLastUpdatedDesc(User participant);

    Room findRoomById(Long roomId);

    List<Room> findRoomsByNameContainingIgnoreCase(String name);


}
