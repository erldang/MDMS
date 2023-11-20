package com.example.backend.controller;


import com.example.backend.dto.*;
import com.example.backend.service.HistoryService;
import com.example.backend.service.TableInfoService;
import com.example.backend.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @Autowired
    TableInfoService tableInfoService;
    @GetMapping
    public ResponseDto<Object> showTables(){
        List<TableInfoDto> tables = tableInfoService.findAllTableInfo();
        List<HashMap<String, String>> tableList = new ArrayList<HashMap<String,String>>();

        for(TableInfoDto tableInfoDto : tables){

            String physicalName = tableInfoDto.getPhysicalTableName();
            String logicalName = tableInfoDto.getLogicalTableName();

            HashMap<String ,String> tmp = new HashMap<String,String>();
            tmp.put("physicalName" , physicalName);
            tmp.put("logicalName" , logicalName);

            tableList.add(tmp);

        }

        Set<HashMap<String,String>> set = new HashSet<HashMap<String,String>>(tableList);
        List<HashMap<String,String>> responseDate = new ArrayList<HashMap<String,String>>(set);

        return ResponseDto.builder().data(responseDate).ok("ok").build();
    }


    @GetMapping("/physicalJoin")
    public ResponseDto<Object> findTableByPhysicalName(@RequestParam("physicalName") String physicalName){

        List<HashMap<String,String>> table = tableService.findTableByPhysicalName(physicalName);
        
        //NO 제외하기시작
        for(HashMap<String,String> hashMap : table){
            hashMap.remove("No");
        }

        //여기다가 용량 조회
        BigDecimal tableSize = tableService.getTableSize(physicalName);


        return ResponseDto.builder().data(table).message(tableSize.toPlainString() + "MB").build();
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
        List<StandardTerminologyDto> standardTerminologyDtoList = tableDto.getStandardTerminologyList();


        //이력 테이블에 넣고
        HistoryDto historyDto = HistoryDto
                .builder()
                .email(email)
                .logicalTableName(logicalTableName)
                .physicalTableName(physicalTableName)
                .registrationDate(registrainDate)
                .build();


        historyService.addHistory(historyDto);

        for(StandardTerminologyDto stdDto : standardTerminologyDtoList){
            TableInfoDto tableInfoDto = TableInfoDto.builder()
                    .logicalTableName(logicalTableName)
                    .physicalTableName(physicalTableName)
                    .num(0)
                    .stdTerminologyDto(stdDto)
                .build();
            tableInfoService.addTableInfo(tableInfoDto);
        }




        return ResponseDto.builder().message(message).ok("ok").build();
    }


}
