package com.example.backend.dto;


import com.example.backend.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {

    private String id;
    private String password;
    private String name;
    private String email;

    public UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();

    }



}
