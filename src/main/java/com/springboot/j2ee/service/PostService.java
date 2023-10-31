package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.Post;

import java.util.List;

public interface PostService {
    Post getInfoPost(Long id);
    List<Post> getAllPost();
    List<Post> getPostByIdUser(Long id);
}
