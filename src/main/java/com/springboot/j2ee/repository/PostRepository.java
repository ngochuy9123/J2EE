package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByOrderByCreatedAtDesc();
    Optional<Post> findById(Long id);
    List<Post> findByUserOrderByCreatedAtDesc(User user);
}
