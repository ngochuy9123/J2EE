package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EPostVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private String content;
    private EPostVisibility visible;
    private String imageUrl;
    private User user;




}
