package com.springboot.j2ee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private String location;
    private String instagram;
    private String twitter;
    private String github;
    private long idUser;

    public UserInfoDTO(String location, String instagram, String twitter, String github) {
        this.location = location;
        this.instagram = instagram;
        this.twitter = twitter;
        this.github = github;
    }
}
