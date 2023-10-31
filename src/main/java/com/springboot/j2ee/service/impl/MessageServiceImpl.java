package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.MessageDTO;
import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.MessageRepository;
import com.springboot.j2ee.repository.RoomRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Message> getMessagesByRoomId(Long id) {
        var room = roomRepository.findRoomById(id);
        return messageRepository.getMessagesByRoomOrderByTimeSend(room);
    }



    @Override
    public void save(MessageDTO messageDTO) {
        Message message = new Message();
        User user = userRepository.findUserById(messageDTO.getUserId());
        Room room = roomRepository.findRoomById(messageDTO.getRoomId());

        message.setUser(user);
        message.setMessage(messageDTO.getMessage());
        message.setRoom(room);
        message.setMessage(messageDTO.getMessage());
        message.setTimeSend(messageDTO.getTimeSend());

        messageRepository.save(message);
    }

    @Override
    public Integer getMissedMessage(Long roomId, Timestamp lastSeen) {
        Room room = roomRepository.findRoomById(roomId);
        return messageRepository.countMessagesByRoomAndTimeSendGreaterThan(room, lastSeen);
    }


}
