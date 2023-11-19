package com.springboot.j2ee.dto;

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
}
