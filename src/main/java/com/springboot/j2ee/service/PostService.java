package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.Post;

import java.util.List;

public interface PostService {
    Post getInfoPost(Long id);
    List<Post> getAllPost(Long id);
    List<Post> getPostByIdUser(Long id);

    List<Post> findPost(String filter);
}
