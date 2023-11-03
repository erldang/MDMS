package com.example.backend.entity;


import com.example.backend.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String password;
    private String name;
    private String email;

    public User(UserDto userDto){
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();

    }




}
