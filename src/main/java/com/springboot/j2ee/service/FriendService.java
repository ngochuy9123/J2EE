package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.Friend;
import jdk.jfr.Frequency;

import java.util.List;

public interface FriendService {
//    dung save
    Friend sendFriendRequest(long idUserFrom,long idUserTo);
//    dung getalllist where status = friend
    List<Friend> displayListFriend(long idUser);
//    dung getalllist where status = sending
    List<Friend> displayFriendRequest(long idUser);
//    lay friend ra roi thay doi status va save
    Friend acceptFriendRequest(long idUserFrom,long idUserTo);
//    remove khoi table
    Friend declineFriendRequest(long idUserFrom,long idUserTo);
//    Tim kiem ban

}
