package com.springboot.j2ee.repository;

import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.entity.Like;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByPostEmoteAndUserEmote(Post post, User user);
    List<Like> findByPostEmote(Post post);

    long countByPostEmote(Post post);

    @Modifying
    @Query(value = "DELETE FROM Like l WHERE l.id = :id")
    void deleteLikeById(@Param("id") long id);

//    Like findLikeByUserAndPost(@Param("user_id") long user_id,@Param("post_id") long post_id);
}
