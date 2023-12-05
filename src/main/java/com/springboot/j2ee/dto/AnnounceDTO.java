package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Announce;
import com.springboot.j2ee.enums.EAnnounceStatus;
import com.springboot.j2ee.enums.EAnnounceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AnnounceDTO {
    private EAnnounceType eAnnounceType;
    private EAnnounceStatus eAnnounceStatus;
    private long idPost;
    private long idUserFrom;
    private long idUserTo;
    private Timestamp creat_at;
    private String avatar;
    private String username;

    public AnnounceDTO(Announce announce) {
        eAnnounceType = announce.getEAnnounceType();
        eAnnounceStatus = announce.getEAnnounceStatus();
        idPost = announce.getPost().getId();
        idUserFrom = announce.getUserFrom().getId();
        idUserTo = announce.getUserTo().getId();
        creat_at = announce.getCreateAt();
        avatar = announce.getUserFrom().getAvatar();
        username = announce.getUserFrom().getUsername();
    }

}
