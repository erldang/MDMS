package com.example.backend.controller;


import com.example.backend.dto.HistoryDto;
import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.TableDto;
import com.example.backend.service.HistoryService;
import com.example.backend.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequestMapping("/table")
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class TableController {

    @Autowired
    TableService tableService;

    @Autowired
    HistoryService historyService;

    @PostMapping("/create")
    public ResponseDto<Object> createTable(@RequestBody TableDto tableDto){
        String message = tableService.createTable(tableDto);


        String email = tableDto.getEmail();
        String tableName = tableDto.getTableName();
        Date registrainDate = new Date();

        HistoryDto historyDto = HistoryDto
                .builder()
                .email(email)
                .tableName(tableName)
                .registrationDate(registrainDate)
                .build();

        historyService.addHistory(historyDto);


        return ResponseDto.builder().message(message).ok("ok").build();
    }


}
