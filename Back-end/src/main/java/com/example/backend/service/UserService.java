package com.example.backend.service;


import com.example.backend.dto.EmailCodeDto;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtProvider;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.nio.file.NoSuchFileException;
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


        session.setAttribute(to , code);
        session.setMaxInactiveInterval(180);




        return code;
    }

//    public String AuthenticationRequestCheck(HttpSession session , EmailCodeDto emailCodeDto) throws MessagingException {
//
//        String email = emailCodeDto.getEmail();
//        System.out.println("check code : " + session.getAttribute(email));
//
//
//        if(emailCodeDto.getCode().equals(session.getAttribute(email))){
//            return "인증완료";
//        }
//        else{
//            return "인증실패";
//        }
//
//    }

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


            //redisTemplate.opsForValue().set(userDto.getEmail(), token, TimeUnit.MILLISECONDS.ordinal());
            //String test = redisTemplate.opsForValue().get(userDto.getEmail());
            return token;

        }
    }

//    public String logOut(UserDto userDto){
//
//        if(redisTemplate.opsForValue().get(userDto.getEmail()) != null ){
//
//            redisTemplate.delete(userDto.getEmail());
//        //redisTemplate.delete(userDto.getEmail());
//        }
//        return "로그아웃 완료";
//    }

    public String modifyUser(UserDto userDto){

       // String test = (String) redisTemplate.opsForValue().get(userDto.getEmail());
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

    public UserDto findByEmail(UserDto userDto){
        String email = userDto.getEmail();

        UserDto u = new UserDto(userRepository.findById(email).get());
        return u;
    }

    //UserDto 2개를 받아서 비교하기 같으면 true 아니면 false
    public Boolean userCheckByToken(UserDto tmp1 , String token){

        String emailFromToken = jwtProvider.getEmailFromToken(token);

        User userTmp= userRepository.findById(emailFromToken).get();

        UserDto tmp2 = new UserDto(userTmp);


        if(tmp1.getPassword().equals(tmp2.getPassword()) &&
                tmp1.getEmail().equals(tmp2.getEmail())){
            return true;
        }

        return false;
    }


    public List<UserDto> findByNameAndPhone(UserDto userDto) {


        //User u =  userRepository.findByNameAndPhone(userDto.getEmail() , userDto.getPhone());
        List<User> userList = userRepository.findByNameAndPhone(userDto.getName() , userDto.getPhone());
        System.out.println(userList.toString());
        List<UserDto> userDtoList =new ArrayList<UserDto>();
        for(User user : userList){
            userDtoList.add(new UserDto(user));
        }


        return userDtoList;

    }

    public UserDto findPassword(UserDto userDto) {

        User user = userRepository.findPassword(userDto.getEmail(),userDto.getName(),userDto.getPhone());


        return  new UserDto(user);
    }

    public Boolean duplicationCheck(UserDto userDto) {

        String email = userDto.getEmail();

        Optional<User> u = userRepository.findById(email);

        System.out.println(u.isEmpty());

        if(u.isEmpty()){
            return false;
        }
        else{
            return true;
        }

    }
}
