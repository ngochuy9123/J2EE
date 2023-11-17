package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    UserInfo findByUserInfo(User user);
}
