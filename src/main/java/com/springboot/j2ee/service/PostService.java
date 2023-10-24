package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> getAllPost();
}
