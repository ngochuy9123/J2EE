package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
