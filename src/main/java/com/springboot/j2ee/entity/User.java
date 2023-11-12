package com.springboot.j2ee.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;

    private String phone;

    private String avatar;

    private String background;

    private String role;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name="updated_at")
    private Timestamp updatedAt;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Post> posts = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "userFrom",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Friend> lsFriendFrom = new ArrayList<>();

    @OneToMany(
            mappedBy = "userTo",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Friend> lsFriendTo = new ArrayList<>();

    @OneToMany(
            mappedBy = "userEmote",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Like> likes = new ArrayList<>();


    public User(String firstName, String lastName, String email, String password, String phone, String role,Timestamp createdAt,Timestamp updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    public Post createPost(Post post){
        this.posts.add(post);
        post.setUser(this);
        return post;
    }

    public void removePost(Post post){
        this.posts.add(post);
        post.setUser(null);
    }


}
