package com.springboot.j2ee.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.j2ee.enums.EPostVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    @OneToMany(
            mappedBy = "postEmote",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Like> likes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EPostVisibility getVisible() {
        return visible;
    }

    public void setVisible(EPostVisibility visible) {
        this.visible = visible;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getCreatedFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = (createdAt != null) ? dateFormat.format(createdAt) : "N/A";
        return formattedDate;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }


}
