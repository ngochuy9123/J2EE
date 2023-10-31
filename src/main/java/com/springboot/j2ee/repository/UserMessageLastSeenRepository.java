package com.springboot.j2ee.repository;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.entity.UserMessageLastSeen;
import com.springboot.j2ee.entity.UserMessageLastSeenId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMessageLastSeenRepository extends JpaRepository<UserMessageLastSeen, UserMessageLastSeenId> {

    Optional<UserMessageLastSeen> findByUserAndRoom(User user, Room room);

}
