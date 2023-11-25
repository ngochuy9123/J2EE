package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.repository.CommentRepository;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment save(Comment cmt) {
        return commentRepository.save(cmt);
    }

    @Override
    public List<Comment> findCommentByPost(Long post_id) {
        List<Comment> comments = commentRepository.findCommentByPost(post_id);
        return comments;
    }

    @Override
    public List<Comment> getCommentByPost(Long idPost) {
        Post post = postRepository.findById(idPost).get();
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }


}
