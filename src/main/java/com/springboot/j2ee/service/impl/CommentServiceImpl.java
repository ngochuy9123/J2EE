package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.repository.CommentRepository;
import com.springboot.j2ee.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    public CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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


}
