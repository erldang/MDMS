package com.example.backend.controller;


import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.TableDto;
import com.example.backend.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/table")
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class TableController {

    @Autowired
    TableService tableService;

    @PostMapping("/create")
    public ResponseDto<Object> createTable(@RequestBody TableDto tableDto){
        System.out.println("contoller 작동 전");
        String message = tableService.createTable(tableDto);
        System.out.println("contoller 작동 후");
        return ResponseDto.builder().message(message).ok("ok").build();
    }


}
