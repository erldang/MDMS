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
import java.util.*;

@RequestMapping("/table")
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class TableController {

    @Autowired
    TableService tableService;

    @Autowired
    HistoryService historyService;

    @GetMapping
    public ResponseDto<Object> showTables(){
        List<HashMap<String,String>> tables = tableService.showTables();
        List<String> tableList = new ArrayList<String>();

        for(HashMap<String,String> table : tables){
            for(Map.Entry<String,String> entry : table.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue());
                tableList.add(entry.getValue());
            }
        }

        return ResponseDto.builder().data(tableList).build();
    }


    @GetMapping("/physicalJoin")
    public ResponseDto<Object> findTableByPhysicalName(@RequestParam("physicalName") String physicalName){

        System.out.println((physicalName));
        List<HashMap<String,String>> table = tableService.findTableByPhysicalName(physicalName);
        return ResponseDto.builder().data(table).message("찾았드아").build();
    }

    @GetMapping("/logicalJoin")
    public ResponseDto<Object> findTableByLogicallName(@RequestParam("logicalName") String logicalName){

        String message = logicalName;
        List<HashMap<String,String>> table = tableService.findTableByLogicalName(logicalName);
        return ResponseDto.builder().data(table).message(message).build();
    }

    @PostMapping("/create")
    public ResponseDto<Object> createTable(@RequestBody TableDto tableDto){
        String message = tableService.createTable(tableDto);

        if(message.equals("중복")){
            return ResponseDto.builder().message("테이블 물리명 중복").ok("no").build();
        }

        String email = tableDto.getEmail();
        String physicalTableName = tableDto.getPhysicalTableName();
        String logicalTableName = tableDto.getLogicalTableName();
        Date registrainDate = new Date();



        HistoryDto historyDto = HistoryDto
                .builder()
                .email(email)
                .logicalTableName(logicalTableName)
                .physicalTableName(physicalTableName)
                .registrationDate(registrainDate)
                .build();

        System.out.println(historyDto.toString());

        historyService.addHistory(historyDto);


        return ResponseDto.builder().message(message).ok("ok").build();
    }


}
