package com.example.backend.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
@Getter
@Setter
public class JwtProvider {
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("HarryPoterSaysWingdadiumRevioooousa".getBytes()); // 시크릿 키
    private final long EXPIRATION_TIME = 864_000_000; // 10일 (단위: 밀리초)

    public String generateToken(String email) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        System.out.println("작동하는");
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256   , SECRET_KEY)
                .compact();
    }

    public String getEmailFromToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

//    public boolean validateToken(String token, String email) {
//        String tokenUsername = getEmailFromToken(token);
//        return (tokenUsername.equals(email) && !isTokenExpired(token));
//    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 유효성 검사 실패
            return false;
        }
    }
    public boolean isTokenExpired(String token) {
//        Date expirationDate = Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();

        Date expirationDate = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expirationDate.before(new Date());
    }




}