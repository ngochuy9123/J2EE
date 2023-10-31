package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public boolean existsByEmail(String email);
    public User findByEmail(String email);

    User findUserById(Long id);
}
