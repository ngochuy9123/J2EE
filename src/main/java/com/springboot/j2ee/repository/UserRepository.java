package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public boolean existsByEmail(String email);
    public User findByEmail(String email);
    public List<User> findByEmailLikeAndIdNot(String email,Long id);
    @Query(value = "SELECT * FROM User u WHERE u.email LIKE CONCAT('%',:email, '%')", nativeQuery = true)
    List<User> findByEmailLike(@Param("email") String email);

    public Optional<User> findById(Long id);

}
