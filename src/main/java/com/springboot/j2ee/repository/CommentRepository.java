package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
