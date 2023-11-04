package com.springboot.j2ee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "emote")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "post_like_fk")
    )
    private Post postLike;

    @ManyToOne
    @JoinColumn(name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_like_fk")
    )
    private User userLike;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
