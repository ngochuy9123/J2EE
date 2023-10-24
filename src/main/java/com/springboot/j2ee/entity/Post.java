package com.springboot.j2ee.entity;

import com.springboot.j2ee.enums.EPostVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "post")
public class Post {

    public Post(String content, EPostVisibility visible, Timestamp createdAt, Timestamp updatedAt) {
        this.content = content;
        this.visible = visible;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Post(String content, EPostVisibility visible, String imageUrl, Timestamp createdAt, Timestamp updatedAt) {
        this.content = content;
        this.visible = visible;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String content;

    @Column(name = "visible",nullable = false)
    private EPostVisibility visible;
    private String imageUrl;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name="updated_at")
    private Timestamp updatedAt;


    @ManyToOne
    @JoinColumn(name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_post_fk")
    )
    private User user;

    @OneToMany(
            mappedBy = "post",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Comment> comments = new ArrayList<>();





}
