package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LikeDTO {
    private long id;
    private long idPost;
    private long idUser;

    public LikeDTO(Like like) {
        id = like.getId();
        idPost = like.getPostEmote().getId();
        idUser = like.getUserEmote().getId();
    }

}
