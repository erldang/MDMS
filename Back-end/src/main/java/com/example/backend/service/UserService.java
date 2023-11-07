package com.example.backend.service;


import com.example.backend.dto.EmailCodeDto;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtProvider;
import com.example.backend.security.RedisRepositoryConfig;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtProvider jwtProvider;

    //@Autowired
    //private RedisTemplate redisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public List<UserDto> getAllUsers(){

        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<UserDto>();
        for(User u : userList){
            userDtoList.add(new UserDto(u));
        }
        return userDtoList;
    }


    public String signUpUser( UserDto userDto){


        User user = new User(userDto);

        userRepository.save(user);

        return "ok";

    }

    public String AuthenticationRequestSend(HttpSession session ,UserDto userDto) throws MessagingException {

        String to = userDto.getEmail();


        String code =  emailService.sendEmailWithAttachment(to);

        System.out.println(code);

        session.setAttribute(to , code);
        session.setMaxInactiveInterval(180);

        System.out.println(session.getAttribute(to));



        return code;
    }

    public String AuthenticationRequestCheck(HttpSession session , EmailCodeDto emailCodeDto) throws MessagingException {

        String email = emailCodeDto.getEmail();
        System.out.println("check code : " + session.getAttribute(email));


        if(emailCodeDto.getCode().equals(session.getAttribute(email))){
            return "인증완료";
        }
        else{
            return "인증실패";
        }

    }

    @Transactional
    public String logIn(UserDto userDto) throws Exception{

        Optional<User> optionalUser = userRepository.findById(userDto.getEmail());

        User user = null;

        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }

        if(user == null){
            return "아이디를 다시 확인해 주세요";
        }

        else{
            if(!userDto.getPassword().equals(user.getPassword())){
                return "비밀번호를 다시 확인해 주세요";
            }

            String token = jwtProvider.generateToken(userDto.getEmail());


            redisTemplate.opsForValue().set(userDto.getEmail(), token, TimeUnit.MILLISECONDS.ordinal());
            String test = redisTemplate.opsForValue().get(userDto.getEmail());
            System.out.println("logIn시 redis에 저장된 토큰확인 : " + test);
            return token;

        }
    }

    public String logOut(UserDto userDto){

        if(redisTemplate.opsForValue().get(userDto.getEmail()) != null ){

            redisTemplate.delete(userDto.getEmail());
        //redisTemplate.delete(userDto.getEmail());
        }
        return "로그아웃 완료";
    }

    public String modifyUser(UserDto userDto){

        String test = (String) redisTemplate.opsForValue().get(userDto.getEmail());
        System.out.println("수정시 redis에 저장된 토큰확인 : " + test);
        User user = userRepository.findById(userDto.getEmail()).get();

        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword());

        userRepository.save(user);

        return "유저 정보 변경 완료";
    }

    public String delete(UserDto userDto){


        userRepository.deleteById(userDto.getEmail());

        return "삭제완료";
    }



}
