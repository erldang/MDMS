package com.example.backend.service;


import com.example.backend.dto.TableDto;
import com.example.backend.repository.TableMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TableService {


    @Autowired
    TableMapper tableMapper;


    public String createTable(TableDto tableDto){

        String query = tableDto.getQuery();

        tableMapper.createTable(query);

        return "테이블 생성 완료";


    }
}
