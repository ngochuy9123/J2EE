package com.springboot.j2ee.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.springboot.j2ee.dto.GenericResponse;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.enums.EFriendStatus;
import com.springboot.j2ee.enums.Emote;
import com.springboot.j2ee.repository.CommentRepository;
import com.springboot.j2ee.repository.MessageRepository;
import com.springboot.j2ee.repository.RoomRepository;
import com.springboot.j2ee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
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

    @Autowired
    LikeService likeService;

    @Autowired
    AnnounceService announceService;

    @Autowired
    FriendService friendService;

    @Autowired
    UserService userService;


    BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "gg");
//    BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "huy123");
    final Map<String, Long> tableMap = new HashMap<String, Long>();


    private final HashMap<Long, HashMap<UUID,Consumer<Message>>> messageSubscribers
            = new HashMap<>() {
    };

    private final HashMap<Long, HashMap<UUID,Consumer<Announce>>> annouceSubscribers
            = new HashMap<>() {
    };

    private final HashMap<Long, HashMap<UUID,Consumer<Friend>>> friendSubscribers
            = new HashMap<>() {
    };

    private final HashMap<Long, HashMap<UUID,Consumer<Friend>>> deleteFriendSubscribers
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

    private final HashMap<UUID,Consumer<Like>> emoteSubscribers
            = new HashMap<>() {
    };

    private final HashMap<UUID,Consumer<Like>> removeEmoteSubscribers
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

    public void subscribeToWriteEmote(UUID uuid, Consumer<Like> consumer) {
        emoteSubscribers.putIfAbsent(uuid, consumer);
    }

    public void handleWriteEmote(WriteRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[0].toString());
            invokeWriteEmote(id);

        }
    }

    private void invokeWriteEmote(Long emoteId) {

        var emote = likeService.findLikeById(emoteId);

        if (emote.isEmpty()) {
            return;
        }

        //FIXME: Gửi tổng :v
        for (var id : emoteSubscribers.keySet()) {
            var consumer = emoteSubscribers.get(id);
            consumer.accept(emote.get());
        }
    }

    public void subscribeToRemoveEmote(UUID uuid, Consumer<Like> consumer) {
        removeEmoteSubscribers.putIfAbsent(uuid, consumer);
    }

    public void handleRemoveEmote(DeleteRowsEventData eventData) {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[2].toString());
            invokeRemoveEmote(id);

        }
    }

    private void invokeRemoveEmote(Long emoteId) {

        var emote = new Like();
        var post = new Post();
        var user = new User();
        post.setId(emoteId);
        user.setId(0L);
        emote.setPostEmote(post);
        emote.setUserEmote(user);

        //FIXME: Gửi tổng :v
        for (var id : removeEmoteSubscribers.keySet()) {
            var consumer = removeEmoteSubscribers.get(id);
            consumer.accept(emote);
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

    public void subscribeToWriteAnnounce(Long userId, UUID uuid,Consumer<Announce> consumer) {
        annouceSubscribers.putIfAbsent(userId, new HashMap<>());
        var eventHandler = annouceSubscribers.get(userId);
        eventHandler.put(uuid, consumer);
    }


    public void handleWriteAnnounce(WriteRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[0].toString());
            invokeWriteAnnounce(id);

        }
    }



    private void invokeWriteAnnounce(Long id) {

        var announce = announceService.getAnnounceById(id);
        sendAnnounceToSubscriber(announce.getUserTo().getId(), announce);


    }

    private void sendAnnounceToSubscriber(Long userId, Announce announce) {
        var eventHandler = annouceSubscribers.get(userId);

        if (eventHandler == null) {
            return;
        }

        for (var key : eventHandler.keySet()) {
            var delegate = eventHandler.get(key);
            delegate.accept(announce);
        }
    }

    public void subscribeToWriteFriend(Long userId, UUID uuid,Consumer<Friend> consumer) {
        friendSubscribers.putIfAbsent(userId, new HashMap<>());
        var eventHandler = friendSubscribers.get(userId);
        eventHandler.put(uuid, consumer);
    }


    public void handleDeleteFriend(DeleteRowsEventData eventData) {
        for (var data: eventData.getRows()) {
            var fId = Long.valueOf(data[4].toString());
            var tId = Long.valueOf(data[3].toString());
            invokeDeleteFriend(fId, tId);

        }
    }

    private void invokeDeleteFriend(Long toId, Long fromId) {

        var friend = new Friend();
        var userTo = userService.getUserById(toId);
        var userFrom = userService.getUserById(fromId);
        friend.setUserTo(userTo);
        friend.setUserFrom(userFrom);
        friend.setStatus(EFriendStatus.SENDING);
        var time = new Timestamp(System.currentTimeMillis());
        friend.setCreatedAt(time);
        friend.setId(-1L);

        sendDeleteFriendToSubscriber(friend.getUserTo().getId(), friend);
    }


    public void subscribeToDeleteFriend(Long userId, UUID uuid,Consumer<Friend> consumer) {
        deleteFriendSubscribers.putIfAbsent(userId, new HashMap<>());
        var eventHandler = deleteFriendSubscribers.get(userId);
        eventHandler.put(uuid, consumer);
    }


    public void handleWriteFriend(WriteRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[0].toString());
            invokeWriteFriend(id);

        }
    }

    private void invokeWriteFriend(Long id) {

        var friend = friendService.findFriendById(id);
        sendFriendToSubscriber(friend.getUserTo().getId(), friend);
    }


    private void sendFriendToSubscriber(Long userId, Friend friend) {
        var eventHandler = friendSubscribers.get(userId);

        if (eventHandler == null) {
            return;
        }

        for (var key : eventHandler.keySet()) {
            var delegate = eventHandler.get(key);
            delegate.accept(friend);
        }
    }

    private void sendDeleteFriendToSubscriber(Long userId, Friend friend) {
        var eventHandler = deleteFriendSubscribers.get(userId);

        if (eventHandler == null) {
            return;
        }

        for (var key : eventHandler.keySet()) {
            var delegate = eventHandler.get(key);
            delegate.accept(friend);
        }
    }

    private void handleWrite(WriteRowsEventData eventData) throws JsonProcessingException {
        if(eventData.getTableId() == tableMap.getOrDefault("message", Long.valueOf("-1"))) {
            handleWriteMessage(eventData);
        }
        if(eventData.getTableId() == tableMap.getOrDefault("comment", Long.valueOf("-1"))) {
            handleWriteComment(eventData);
        }
        if(eventData.getTableId() == tableMap.getOrDefault("emote", Long.valueOf("-1"))) {
            handleWriteEmote(eventData);
        }
        if(eventData.getTableId() == tableMap.getOrDefault("announce", Long.valueOf("-1"))) {
            handleWriteAnnounce(eventData);
        }
        if(eventData.getTableId() == tableMap.getOrDefault("friend", Long.valueOf("-1"))) {
            handleWriteFriend(eventData);
        }
    }

    private void handleRemove(DeleteRowsEventData eventData) {
        if(eventData.getTableId() == tableMap.getOrDefault("emote", Long.valueOf("-1"))) {
            handleRemoveEmote(eventData);
        }
        if(eventData.getTableId() == tableMap.getOrDefault("friend", Long.valueOf("-1"))) {
            handleDeleteFriend(eventData);
        }
    }



    public void handleWriteMessage(WriteRowsEventData eventData) throws JsonProcessingException {
        for (var data: eventData.getRows()) {
            var id = Long.valueOf(data[4].toString());
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
            if (data instanceof DeleteRowsEventData eventData) {
                handleRemove(eventData);
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
