package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EFriendStatus;
import com.springboot.j2ee.repository.FriendRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.FriendService;
import com.springboot.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Friend sendFriendRequest(long idUserFrom, long idUserTo) {
        User userTo = userRepository.findById(idUserTo).get();
        User userFrom = userRepository.findById(idUserFrom).get();

        Friend friend = new Friend();
        friend.setUserTo(userTo);
        friend.setUserFrom(userFrom);
        friend.setStatus(EFriendStatus.SENDING);
        friend.setCreatedAt(timestamp);
        return friendRepository.save(friend);
    }

    @Override
    public List<Friend> displayListFriend(long idUser) {
        User user = userRepository.findById(idUser).get();
        return friendRepository.findByUserFromAndStatusOrUserToAndStatus(user,EFriendStatus.FRIEND,user,EFriendStatus.FRIEND);
    }

    @Override
    public List<Friend> displayFriendRequest(long idUser) {
        User user = userRepository.findById(idUser).get();
        return friendRepository.findByUserToAndStatusOrderByCreatedAt(user,EFriendStatus.SENDING);
    }

    @Override
    public Friend acceptFriendRequest(long idUserFrom, long idUserTo) {
        User userFrom = userRepository.findById(idUserFrom).get();
        User userTo = userRepository.findById(idUserTo).get();
        Friend friend = friendRepository.findByUserToAndUserFrom(userTo,userFrom);
        if (friend == null){
            friend = friendRepository.findByUserToAndUserFrom(userFrom,userTo);
        }
        friend.setStatus(EFriendStatus.FRIEND);
        return friendRepository.save(friend);
    }

    @Override
    public Friend declineFriendRequest(long idUserFrom, long idUserTo) {
        User userFrom = userRepository.findById(idUserFrom).get();
        User userTo = userRepository.findById(idUserTo).get();
//        co the rut gon vi delete chi co 1 ben co the delete la userTo
        Friend friend = friendRepository.findByUserToAndUserFrom(userTo,userFrom);
        if (friend == null){
            friend = friendRepository.findByUserToAndUserFrom(userFrom,userTo);
        }

        System.out.println(friend.getId());

        friendRepository.deleteById(friend.getId());

        return friend;

    }
}
