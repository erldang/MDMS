package com.example.backend.service;


import com.example.backend.security.JwtProvider;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendEmailWithAttachment(
            String to
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("lshlsh2637@naver.com");
        helper.setTo(to);

        String code = generateCertificationNumber();

        helper.setText(code);

        System.out.println("이메일로 보낸 코드 : " + code);
        mailSender.send(message);

        return code;
    }


    public String generateCertificationNumber(){

        Random random = new Random();
        int createNum = 0;
        String ranNum = "";
        int letter = 6;
        String resultNum = "";

        for(int i = 0; i<6;i++){
            createNum = random.nextInt(9);
            ranNum = Integer.toString(createNum);
            resultNum += ranNum;
        }

        return resultNum;
    }
}