package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByOrderByCreatedAtDesc();
    Optional<Post> findById(Long id);
}
