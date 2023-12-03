package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EPostVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByOrderByCreatedAtDesc();
    Optional<Post> findById(Long id);
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    List<Post> findByUserAndVisibleOrderByCreatedAtDesc(User user, EPostVisibility visibility);
    List<Post> findByVisibleOrderByCreatedAtDesc(EPostVisibility visibility);

    @Query(value = "SELECT * FROM post p WHERE p.content LIKE CONCAT('%',:filter, '%') and visible <> 0 order by p.created_at desc ", nativeQuery = true)
    List<Post> findPostByContent(@Param("filter") String filter);

//    Lay tat ca bai post công khai
    @Query(value = "SELECT * FROM post p WHERE p.content LIKE CONCAT('%',:filter, '%') and user_id <> :user_id and visible = 2 order by p.created_at desc ", nativeQuery = true)
    List<Post> getAllPublicPostOtherUser(@Param("user_id") long user_id, @Param("filter") String filter);

    @Query(value = "SELECT * FROM post p WHERE p.content LIKE CONCAT('%',:filter, '%') and user_id = :user_id and visible = :visible order by p.created_at desc ", nativeQuery = true)
    List<Post> getListUserPostByVisible(@Param("user_id") long user_id, @Param("visible") int visible, @Param("filter") String filter);

    @Query(value = "SELECT * FROM post p WHERE user_id = :user_id order by p.created_at desc ", nativeQuery = true)
    List<Post> getAllPostByUserId(@Param("user_id") long user_id);
}
