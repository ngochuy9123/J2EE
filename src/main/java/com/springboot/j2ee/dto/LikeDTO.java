package com.springboot.j2ee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LikeDTO {
    private Long id;
    private long idPost;
    private long idUser;
}