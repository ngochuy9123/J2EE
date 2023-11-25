package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(value = "SELECT * FROM Comment c where c.post_id = :post_id", nativeQuery = true)
    List<Comment> findCommentByPost(@Param("post_id") Long post_id);
}
