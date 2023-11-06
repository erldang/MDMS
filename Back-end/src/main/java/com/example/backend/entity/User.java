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
    private String email;
    private String password;
    private String name;
    private String phone;

    public User(UserDto userDto){
        this.email = userDto.getEmail();
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.phone = userDto.getPhone();

    }




}
