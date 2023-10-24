package com.springboot.j2ee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "post_image")
public class Post_Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String url;
//
//    @ManyToOne
//    @JoinColumn(name = "post_id",
//            nullable = false,
//            referencedColumnName = "id",
//            foreignKey = @ForeignKey(name = "post_post_image_fk")
//    )
//    private Post post;
}
