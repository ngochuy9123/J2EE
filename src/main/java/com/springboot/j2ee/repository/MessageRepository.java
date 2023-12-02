package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findMessageById(Long id);


    List<Message> getMessagesByRoomOrderByTimeSend(Room room);

    Integer countMessagesByRoomAndTimeSendGreaterThan(Room room, Timestamp timestamp);
}
