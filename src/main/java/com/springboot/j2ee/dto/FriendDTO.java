package com.springboot.j2ee.dto;

import com.springboot.j2ee.enums.EFriendStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {
    private Long idUserFrom;
    private Long getIdUserTo;
    private EFriendStatus status;
    private Timestamp createdAt;
}
