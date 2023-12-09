package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.AnnounceId;
import com.springboot.j2ee.entity.Announce;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EAnnounceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnounceRepository extends JpaRepository<Announce, Long> {
    public List<Announce> findByUserToOrderByCreateAtDesc(User userTo);
    public List<Announce> findByUserToAndUserFromAndPost(User userTo, User userFrom, Post post);

    public Announce findAnnounceById(Long id);

    List<Announce> findByPost(Post post);

    void deleteById(Long id);

}
