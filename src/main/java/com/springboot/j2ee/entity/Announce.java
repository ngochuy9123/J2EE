package com.springboot.j2ee.entity;

import com.springboot.j2ee.enums.EAnnounceStatus;
import com.springboot.j2ee.enums.EAnnounceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "announce")
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private EAnnounceStatus eAnnounceStatus;


    private EAnnounceType eAnnounceType;

    @ManyToOne
    @JoinColumn(name = "id_post",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "post_announce_fk")
    )
    private Post post;
//    thong tin user like hay comment
    @ManyToOne
    @JoinColumn(name = "id_user_from",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "userFrom_announce_fk")
    )
    private User userFrom;
//    user can duoc thong bao
    @ManyToOne
    @JoinColumn(name = "id_user_to",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "userTo_announce_fk")
    )
    private User userTo;

    @Column(name = "created_at")
    private Timestamp createAt;
}
