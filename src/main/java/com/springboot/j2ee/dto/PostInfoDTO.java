package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostInfoDTO {
    private long id;
    private String content;
    private String image;

    private UserDTO user;
    private Timestamp created_at;
    private List<CommentDetailDTO> lstComment;
    private long numLikes;
    private boolean liked;



}
