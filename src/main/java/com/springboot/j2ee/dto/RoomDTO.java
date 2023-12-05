package com.springboot.j2ee.dto;

import com.springboot.j2ee.entity.Room;
import com.springboot.j2ee.enums.ERoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;

    private String name;

    private ERoomType roomType;

    private List<UserDTO> participants = new ArrayList<>();

    private List<MessageDTO> messages = new ArrayList<>();

    private Timestamp lastUpdated;
    private String roomAvatar;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.roomType = room.getRoomType();
        for (var p : room.getParticipants()) {
            this.participants.add(new UserDTO(p));
        }

        for (var m : room.getMessages()) {
            this.messages.add(new MessageDTO(m));
        }
        this.lastUpdated = room.getLastUpdated();

        this.roomAvatar = room.getRoomAvatar();
    }
}
