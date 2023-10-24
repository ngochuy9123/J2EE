package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RoomRepository extends JpaRepository<Room, Long> {

    ArrayList<Room> findRoomsByParticipantsContainsOrderByLastUpdated(User participant);


}
