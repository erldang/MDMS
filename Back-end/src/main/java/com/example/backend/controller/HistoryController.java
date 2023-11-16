package com.example.backend.controller;


import com.example.backend.dto.HistoryDto;
import com.example.backend.dto.ResponseDto;
import com.example.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @GetMapping
    public ResponseDto<Object> findAllHistoru(){

        List<HistoryDto> historyDtoList = historyService.findAllHistory();


        return ResponseDto.builder().data(historyDtoList).build();
    }

    @GetMapping("/join")
    public ResponseDto<Object> findHistoryByEmail(@RequestParam("email") String email){

        List<HistoryDto> historyDtoList = historyService.findHistoryByEmail(email);
        return ResponseDto.builder().data(historyDtoList).build();

    }



}
