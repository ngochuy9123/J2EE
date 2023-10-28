package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EFriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {
    List<Friend> findByUserToAndStatusOrderByCreatedAt(User userTo, EFriendStatus eFriendStatus);

    List<Friend> findByUserFromOrUserToAndStatus(User userFrom,User userTo,EFriendStatus eFriendStatus);
}
