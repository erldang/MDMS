package com.example.backend.controller;


import com.example.backend.dto.EmailCodeDto;
import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.UserDto;
import com.example.backend.service.EmailService;
import com.example.backend.security.JwtProvider;
import com.example.backend.service.HistoryService;
import com.example.backend.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserContoller {


    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    HistoryService historyService;

    //일반사용자가입
    @PostMapping("/signup")
    public ResponseDto<Object> signUpUser(@RequestBody UserDto userDto) throws Exception {

        userDto.setAdmin(false);
        String response = userService.signUpUser(userDto);


        return ResponseDto.builder().message(response).ok("ok").build();

    }

    @PostMapping("/authentication/send")
    public ResponseDto<Object> AuthenticationRequestSend(HttpSession session,
                                            @RequestBody UserDto userDto) throws MessagingException {

        String response =  userService.AuthenticationRequestSend(session, userDto);

        return ResponseDto.builder().message(response).ok("ok").build();

    }


    @PostMapping("/login")
    public ResponseDto<Object> logIn( @RequestBody UserDto userDto) throws Exception {

        String message = userService.logIn(userDto);
//        String token = null;
//        if(message.equals("로그인 성공")){
//            token = JwtProvider.generateToken(userDto.getEmail());
//        }

        UserDto user = userService.findByEmail(userDto);



        //System.out.println(token);
        ResponseDto<Object> response = ResponseDto.builder().data(user).ok("ok").message(message).build();
        return response;

    }

    @PostMapping("/modify")
    public ResponseDto<Object> modify(@RequestBody UserDto userDto) throws Exception {

        String message = userService.modifyUser(userDto);
        return ResponseDto.builder().ok("ok").message(message).build();
    }

//    @PostMapping("/logout")
//    public ResponseDto<Object> logout(@RequestBody UserDto userDto){
//
//        //String message = userService.logout(userDto);
//        String message = userService.logOut(userDto);
//
//        return ResponseDto.builder().ok("ok").message(message).build();
//
//    }

    @PostMapping("/check")
    public ResponseDto<Object> deleteCheck(@RequestBody UserDto userDto,
                                           @RequestParam("token") String token){

        Boolean message = userService.userCheckByToken(userDto , token);

        return ResponseDto.builder().ok("ok").data(message).message("검사 완료").build();
    }

    @DeleteMapping("/delete")
    public ResponseDto<Object> delete(@RequestBody UserDto userDto){

        String message = userService.delete(userDto);



        return ResponseDto.builder().ok("ok").message(message).build();

    }

    @GetMapping
    public ResponseDto<Object> getAllUsers() throws Exception {

        //userService.getAllUsers();
        return ResponseDto.builder().ok("ok").data(userService.getAllUsers()).build();
    }


    @GetMapping("/specific")
    public ResponseDto<Object> getSpecifyUser(@RequestParam("token") String token) throws Exception {

        String email = jwtProvider.getEmailFromToken(token);

        UserDto user = userService.findByEmail(UserDto.builder().email(email).build());

        return ResponseDto.builder().ok("ok").data(user).build();

    }

    @PostMapping("/findId")
    public ResponseDto<Object> findId(@RequestBody UserDto userDto){

        List<UserDto> userDtoList = userService.findByNameAndPhone(userDto);
        List<UserDto> data = new ArrayList<UserDto>();
        for(UserDto user : userDtoList){
            data.add(UserDto.builder().email(user.getEmail()).build());
        }


        return ResponseDto.builder().ok("ok").data(data).build();
    }

    @PostMapping("/findPwd")
    public ResponseDto<Object> findPwd(@RequestBody UserDto userDto) throws Exception {

        UserDto user = userService.findPassword(userDto);

        user.setName(null);
        user.setEmail(null);
        user.setAdmin(null);
        user.setPhone(null);

        return ResponseDto.builder().ok("ok").data(user).build();
    }

    //아이디 중복확인
    @PostMapping("/duplicationCheck")
    public ResponseDto<Object> duplicationCheck(@RequestBody UserDto userDto){

        Boolean isDuplicated = userService.duplicationCheck(userDto);

        String message = isDuplicated ? "중복" : "미중복";

        return ResponseDto.builder().data(isDuplicated).message(message).build();
    }



}
