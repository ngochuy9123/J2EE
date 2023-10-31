package com.springboot.j2ee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@IdClass(UserMessageLastSeenId.class)
public class UserMessageLastSeen {
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Room room;

    private Timestamp lastSeen;


}
