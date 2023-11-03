package com.example.backend.service;


import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String signUpUser(UserDto userDto){

        System.out.println("service : " + userDto.toString());

        User user = new User(userDto);

        userRepository.save(user);

        return "ok";

    }

}
