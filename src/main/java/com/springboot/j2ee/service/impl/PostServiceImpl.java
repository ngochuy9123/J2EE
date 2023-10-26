package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.service.PostService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public Post getInfoPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public List<Post> getAllPost() {
        return postRepository.findByOrderByCreatedAtDesc();
    }



}
