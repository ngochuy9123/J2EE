package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.SearchHistory;
import com.springboot.j2ee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {
    List<SearchHistory> findByValueAndUser(String value, User user);

}
