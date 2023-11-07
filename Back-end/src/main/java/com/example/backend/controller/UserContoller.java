package com.example.backend.controller;


import com.example.backend.dto.EmailCodeDto;
import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.UserDto;
import com.example.backend.service.EmailService;
import com.example.backend.security.JwtProvider;
import com.example.backend.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserContoller {


    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseDto<Object> signUpUser(@RequestBody UserDto userDto) {

        String response = userService.signUpUser(userDto);

        return ResponseDto.builder().message(response).ok("ok").build();

    }

    @PostMapping("/authentication/send")
    public ResponseDto<Object> AuthenticationRequestSend(HttpSession session,
                                            @RequestBody UserDto userDto) throws MessagingException {

        String response =  userService.AuthenticationRequestSend(session, userDto);

        return ResponseDto.builder().message(response).ok("ok").build();

    }

    @PostMapping("/authentication/check")
    public ResponseDto<Object> AuthenticationRequestCheck(HttpSession session,
                                             @RequestBody EmailCodeDto emailCodeDto) throws MessagingException {

        String response =  userService.AuthenticationRequestCheck(session, emailCodeDto);

        return ResponseDto.builder().message(response).ok("ok").build();

    }

    @PostMapping("/login")
    public ResponseDto<Object> logIn( @RequestBody UserDto userDto) throws Exception {

        System.out.println("로그인 contoller 시작");
        String token = userService.logIn(userDto);
//        String token = null;
//        if(message.equals("로그인 성공")){
//            token = JwtProvider.generateToken(userDto.getEmail());
//        }



        //System.out.println(token);
        ResponseDto<Object> response = ResponseDto.builder().ok("ok").message(token).build();
        return response;

    }

    @PostMapping("/modify")
    public ResponseDto<Object> modify(@RequestBody UserDto userDto){


        String message = userService.modifyUser(userDto);
        return ResponseDto.builder().ok("ok").message(message).build();
    }

    @PostMapping("/logout")
    public ResponseDto<Object> logout(@RequestBody UserDto userDto){

        //String message = userService.logout(userDto);
        System.out.println("logout 동작");
        String message = userService.logOut(userDto);



        return ResponseDto.builder().ok("ok").message(message).build();

    }

    @DeleteMapping("/delete")
    public ResponseDto<Object> delete(@RequestBody UserDto userDto){

        String message = userService.delete(userDto);



        return ResponseDto.builder().ok("ok").message(message).build();

    }

    @GetMapping
    public ResponseDto<Object> getAllUsers(){

        //userService.getAllUsers();
        return ResponseDto.builder().ok("ok").data(userService.getAllUsers()).build();
    }

}
