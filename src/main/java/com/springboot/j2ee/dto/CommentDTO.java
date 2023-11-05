package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private Long user_id;
    private Long post_id;

    private String content;
    private String user_avatar;
    private String user_name;

    public CommentDTO(Comment comment){
        id = comment.getId();
        user_id = comment.getUser().getId();
        post_id = comment.getPost().getId();
        content = comment.getContent();
        user_name = comment.getUser().getLastName()+" "+comment.getUser().getFirstName();
        user_avatar = comment.getUser().getAvatar();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
