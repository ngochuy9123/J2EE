package com.springboot.j2ee.repository;

import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByOrderByCreatedAtDesc();
}
