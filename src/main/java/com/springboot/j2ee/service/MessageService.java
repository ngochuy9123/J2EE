package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.MessageDTO;
import com.springboot.j2ee.entity.Message;

import java.sql.Timestamp;
import java.util.List;

public interface MessageService {
    List<Message> getMessagesByRoomId(Long id);

    void save(MessageDTO messageDTO);



    Integer getMissedMessage(Long roomId, Timestamp lastSeen);
}
