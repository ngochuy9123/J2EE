package com.springboot.j2ee.repository;

import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.entity.Like;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByPostEmoteAndUserEmote(Post post, User user);
    List<Like> findByPostEmote(Post post);

    long countByPostEmote(Post post);
}
