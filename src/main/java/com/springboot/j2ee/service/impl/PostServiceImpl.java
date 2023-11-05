package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.PostService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Post getInfoPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public List<Post> getAllPost() {
        return postRepository.findByOrderByCreatedAtDesc();
    }

    @Override
    public List<Post> getPostByIdUser(Long id) {
        User user = userRepository.findById(id).get();
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Post> findPost(String filter) {
        return postRepository.findPostByContent(filter);
    }


}
