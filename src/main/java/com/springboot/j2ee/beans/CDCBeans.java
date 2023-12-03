package com.springboot.j2ee.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.springboot.j2ee.dto.GenericResponse;
import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.entity.Message;
import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.CommentRepository;
import com.springboot.j2ee.repository.MessageRepository;
import com.springboot.j2ee.repository.RoomRepository;
import com.springboot.j2ee.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CDCBeans extends Thread{

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    MessageRepository messageRepository;

//    BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "gg");
    BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "huy123");
    final Map<String, Long> tableMap = new HashMap<String, Long>();


    private final HashMap<Long, HashMap<UUID,Consumer<Message>>> messageSubscribers
            = new HashMap<>() {
    };

    private final HashMap<Long,
            HashMap<UUID,
                    Consumer<GenericResponse<Room>
                            >
                    >
            > userRoomSubscribers = new HashMap<>();

    private final HashMap<UUID,Consumer<Comment>> commentSubscribers
            = new HashMap<>() {
    };




    public CDCBeans() {

    }

    public void subscribeToWriteComment(UUID uuid, Consumer<Comment> consumer) {
        commentSubscribers.putIfAbsent(uuid, consumer);
    }

    public void handleWriteComment(WriteRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[0].toString());
            invokeWriteComment(id);

        }
    }

    private void invokeWriteComment(Long commentId) {

        var comment = commentService.getCommentById(commentId);

        //FIXME: Gửi tổng :v
        for (var id : commentSubscribers.keySet()) {
            var consumer = commentSubscribers.get(id);
            consumer.accept(comment);
        }
    }


    public void subscribeToWriteMessage(Long userId, UUID uuid,Consumer<Message> consumer) {
        var eventHandler = messageSubscribers.get(userId);
        if (eventHandler == null) {
            messageSubscribers.put(userId, new HashMap<>());
            eventHandler = messageSubscribers.get(userId);

        }
        eventHandler.put(uuid, consumer);
    }

    public void subscribeToRoom(Long userId, UUID uuid,Consumer<GenericResponse<Room>> consumer) {
        var eventHandler = userRoomSubscribers.get(userId);
        if (eventHandler == null) {
            userRoomSubscribers.put(userId, new HashMap<>());
            eventHandler = userRoomSubscribers.get(userId);

        }

        eventHandler.put(uuid, consumer);
    }

    private void handleWrite(WriteRowsEventData eventData) throws JsonProcessingException {
        if(eventData.getTableId() == tableMap.getOrDefault("message", Long.valueOf("-1"))) {
            handleWriteMessage(eventData);
        }
        if(eventData.getTableId() == tableMap.getOrDefault("comment", Long.valueOf("-1"))) {
            handleWriteComment(eventData);
        }

    }



    public void handleWriteMessage(WriteRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[3].toString());
            var mId = Long.valueOf(data[0].toString());
            invokeMessageWrite(id, mId);

        }
    }


    private void invokeMessageWrite(Long roomId, Long messageId) {

        Room room = roomRepository.findRoomById(roomId);


        //get User ID = 0 for system to update last updated
        var systemRoomHandler =  userRoomSubscribers.get(0L);
        for (var key : systemRoomHandler.keySet()) {
            var delegate = systemRoomHandler.get(key);
            var response =  new GenericResponse<Room>("MESSAGE_SENT", room);
            delegate.accept(response);
        }


        for (User user : room.getParticipants()) {
            sendMessageToSubscriber(user.getId(), messageId);
        }


    }

    private void sendMessageToSubscriber(Long userId, Long messageId) {
        var eventHandler = messageSubscribers.get(userId);

        if (eventHandler == null) {
            return;
        }

        Message message = messageRepository.findMessageById(messageId);

        for (var key : eventHandler.keySet()) {
            var delegate = eventHandler.get(key);
            delegate.accept(message);
        }
    }



    private void handleUpdate(UpdateRowsEventData eventData) throws JsonProcessingException {
//        if(eventData.getTableId() == tableMap.get("user")) {
//            handleUpdateUser(eventData);
//        }

        if(eventData.getTableId() == tableMap.getOrDefault("room", Long.valueOf("-1"))) {
            handleUpdateRoom(eventData);
        }
    }

    private void handleUpdateRoom(UpdateRowsEventData eventData) {
        var a = eventData.getIncludedColumnsBeforeUpdate();
        var c = eventData.getRows();
        var b = 2;

    }

    public void handleUpdateUser(UpdateRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {

            var a = 2;
        }
    }

    @Override
    public void run() {
        client.registerEventListener(e -> {
            var data = e.getData();

            if(data instanceof TableMapEventData tableData) {
                tableMap.put(tableData.getTable(), tableData.getTableId());
            }

            if (data instanceof WriteRowsEventData eventData) {
                try {
                    handleWrite(eventData);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (data instanceof UpdateRowsEventData eventData) {
                try {
                    handleUpdate(eventData);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }

            System.out.println(data);
            var a = 2;
        });
        try {
            client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("a");

    }

    public void invokeRoomWithType(Long roomId, String type) {
        Room room = roomRepository.findRoomById(roomId);

        for (var user : room.getParticipants()) {
            var id = user.getId();

            var delegates = userRoomSubscribers.getOrDefault(id, null);
            if (delegates == null) {
                continue;
            }

            for (var key : delegates.keySet()) {
                var delegate = delegates.get(key);
                delegate.accept(new GenericResponse<>(type, room));
            }

        }

    }
}
