package com.example.backend.controller;


import com.example.backend.dto.UserDto;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/user")
public class UserContoller {


    @Autowired
    UserService userService;


    @PostMapping
    public String signUpUser(@RequestBody UserDto userDto){

        System.out.println("singUpUser 작동 됨");
        System.out.println(userDto.toString());


        String response = userService.signUpUser(userDto);

        return response;



    }
}
