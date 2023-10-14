package com.springboot.j2ee.entity;

import com.springboot.j2ee.enums.EPostVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;
    @Column(name = "visible",nullable = false)
    private EPostVisibility visible;


    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;


    @OneToMany(mappedBy = "post")
    private ArrayList<Post_Image> dsImage;

    @OneToMany(mappedBy = "post")
    private ArrayList<Post_Video> dsVideo;


}
