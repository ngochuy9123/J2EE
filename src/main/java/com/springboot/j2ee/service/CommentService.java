package com.springboot.j2ee.service;

import com.springboot.j2ee.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment save(Comment cmt);

    List<Comment> findCommentByPost(Long post_id);
}
