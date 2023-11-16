package com.springboot.j2ee.dto;

import com.springboot.j2ee.api.UserAPI;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email khong dung dinh dang")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty(message = "Phone should not be empty")
    private String phone;
    private String avatar;
    private String background;

    public UserDTO(User user) {
        id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = "";
        this.phone = user.getPhone();
        this.avatar = user.getAvatar();
    }

    public UserDTO(User user, String password) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = password;
        this.phone = user.getPhone();
        this.avatar = user.getAvatar();
    }

}
