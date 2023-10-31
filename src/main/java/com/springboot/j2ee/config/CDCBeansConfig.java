package com.springboot.j2ee.config;

import com.springboot.j2ee.beans.CDCBeans;
import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.ERoomType;
import com.springboot.j2ee.repository.MessageRepository;
import com.springboot.j2ee.repository.RoomRepository;
import com.springboot.j2ee.repository.UserRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

@Configuration
public class CDCBeansConfig {

    @Autowired
    RoomRepository roomRepository;


    @Autowired
    MessageRepository messageRepository;


    @Autowired
    UserRepository userRepository;

    private void updateRoom(Room room)
    {
        room.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        roomRepository.save(room);
    }
    @Bean
    public CDCBeans cdcBeans() throws IOException {
        var cdc = new CDCBeans();
        cdc.subscribeToWriteMessageByRoom(this::updateRoom);
        cdc.start();
        return cdc;
    }

}
