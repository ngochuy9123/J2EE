package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.entity.Like;
import com.springboot.j2ee.entity.Post;

import java.util.List;

public interface LikeService {
    Like saveLike(LikeDTO likeDTO);

    void disLike(Like like);
    Like findLike(LikeDTO likeDTO);

    List<Like> getAllEmoteByPostID(Post post);

    long getAllLikeByPostId(Post post);



}
