package com.example.backend.dto;


import com.example.backend.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {

    private String email;
    private String password;
    private String name;
    private String phone;
    private Boolean admin;

    public UserDto(User user){
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.admin = user.getAdmin();
    }
}
