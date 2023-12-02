package com.springboot.j2ee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailDTO {
    private Long idUser;
    private String email;
    private String avatar;
    private String contentComment;
    private Timestamp create_at;
}
