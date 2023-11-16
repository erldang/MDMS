package com.example.backend.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
@Log4j2
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;


    //@Autowired
    //private RedisTemplate redisTemplate;
    // private RedisRepositoryConfig redisRepositoryConfig;



    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Token에서 Claim 꺼내기
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ") ||
                authorizationHeader.equals("Bearer null") )
        {      //header에 AUTHORIZATION이 없거나, Bearer로 시작하지 않으면 filter
            System.out.println("header가 없거나 형식이 틀립니다");
            filterChain.doFilter(request, response);
            return;
        }


        String token;
        try {
            token = authorizationHeader.split(" ")[1].trim();
        } catch (Exception e) {
            log.error("토큰을 분리하는데 실패했습니다. - {}", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }
        //log.info("token : {}", token);

        //토큰이 Valid한지 확인하기
        if(jwtProvider.isTokenExpired(token)){
            filterChain.doFilter(request, response);
            return;
        }

        if(!jwtProvider.validateToken(token)){
            filterChain.doFilter(request, response);
            return;
        }

        //userName 넣기, 문 열어주기
        String email = jwtProvider.getEmailFromToken(token);


        UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(email)));
        //디테일 설정하기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }


    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }


}