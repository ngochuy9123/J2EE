package com.springboot.j2ee.entity;

import com.springboot.j2ee.enums.EFriendStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "friend")

public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_from_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_from_fk")
    )
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_to_fk")
    )
    private User userTo;
    private EFriendStatus status;
    @Column(name = "created_at")
    private Timestamp createdAt;

}
