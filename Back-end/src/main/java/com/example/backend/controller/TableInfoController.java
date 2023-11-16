package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.security.JwtProvider;
import com.example.backend.service.HistoryService;
import com.example.backend.service.TableInfoService;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tableInfo")
@Slf4j
public class TableInfoController {

    @Autowired
    TableInfoService tableInfoService;

    @Autowired
    HistoryService historyService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserService userService;

    //모든 테이블 info 조회하기
    @GetMapping
    public ResponseDto<Object> findAllTableInfo( @RequestHeader Map<String, String> headers){
        List<TableInfoDto> tableinfoDtoList = new ArrayList<TableInfoDto>();
        List<ResponseTableInfoDto> responseTableInfoDtoList = new ArrayList<ResponseTableInfoDto>();
        boolean isAdmin = isAdmin(headers);
        String email = getEmailFromHeader(headers);
        String message = "";
        //관리자면 table_info의 모든 정보를 주기
        if(isAdmin){
            tableinfoDtoList = tableInfoService.findAllTableInfo();
            message = "관리자";



        }

        //사용자면 사용자가만든 table_info의 정보만 주기
        else{
            List<HistoryDto> historyDtoList = historyService.findHistoryByEmail(email);
            List<TableInfoDto> tmpList = new ArrayList<TableInfoDto>();

            for(HistoryDto historyDto : historyDtoList){
                tmpList.addAll(tableInfoService.findTableInfoByTableName(historyDto.getLogicalTableName()));
            }

            tableinfoDtoList = tmpList;
            message = "사용자";
        }


        responseTableInfoDtoList = convertTableInfo(tableinfoDtoList);

        return ResponseDto.builder().ok("ok").data(responseTableInfoDtoList).message(message).build();
    }

    
    //tableInfo where = terminology
    @GetMapping("/join/terminology")
    public ResponseDto<Object> findTableInfoByStdTerminology(@RequestParam(value = "standardTerminology") String standardTerminology,
                                                             @RequestHeader Map<String, String> headers){

        boolean isAdmin = isAdmin(headers);
        String email = getEmailFromHeader(headers);

        List<TableInfoDto> tableInfoDtoList = new ArrayList<TableInfoDto>();

        //관리자면 tableInfo에서 공통표준용어 조회
        if(isAdmin){
            tableInfoDtoList = tableInfoService.findTableInfoByStdTerminology(standardTerminology);


        }
        //사용자면 사용자가 만든 테이블중에서 공통표준용어 조회
        else{


            List<HistoryDto> historyDtoList = historyService.findHistoryByEmail(email);

            //log.info(historyDtoList.toString());

            List<TableInfoDto> tmpList = new ArrayList<TableInfoDto>();

            for(HistoryDto historyDto : historyDtoList){
                //tmpList.addAll(tableInfoService.findTableInfoByTableName(historyDto.get()));
                tmpList.addAll(tableInfoService.findTableInfoByTableName(historyDto.getLogicalTableName()));
            }

            //log.info("tmpList : " + tmpList.toString());
            for(TableInfoDto tableInfoDto : tmpList){
                if(tableInfoDto.getStdTerminologyDto().getStandardTerminology().equals(standardTerminology)){
                    tableInfoDtoList.add(tableInfoDto);
                }
            }

            //log.info("tmpList : " + tableInfoDtoList );
        }




        return ResponseDto.builder().data(convertToTableInfoDtoTerm(tableInfoDtoList)).message(standardTerminology).ok("ok").build();
        //return ResponseDto.builder().data(tableInfoDtoList).message(standardTerminology).ok("ok").build();
    }

    @GetMapping("/join/table")
    public ResponseDto<Object> findTableInfoByTableName(@RequestParam(value = "tableName") String tableName,
                                                        @RequestHeader Map<String, String> headers){

        List<TableInfoDto> tableInfoDtoList = tableInfoService.findTableInfoByTableName(tableName);


        return ResponseDto.builder().data(tableInfoDtoList).message(tableName).ok("ok").build();
    }

    //특정 회원이 생성한 테이블만 조회
    @GetMapping("/user")
    public ResponseDto<Object> findTableInfoByEmail(@RequestParam(value = "email") String email){


        List<HistoryDto> historyDtoList = historyService.findHistoryByEmail(email);
        List<String> tableNameList= new ArrayList<String>();
        for(HistoryDto historyDto : historyDtoList){
            tableNameList.add(historyDto.getLogicalTableName());
        }

        List<TableInfoDto> tableInfoDtoList = new ArrayList<TableInfoDto>();

        for(String tableName : tableNameList){
            List<TableInfoDto> tmpList = tableInfoService.findTableInfoByTableName(tableName);
            tableInfoDtoList.addAll(tmpList);
        }



        return ResponseDto.builder().data(tableInfoDtoList).message(email + "의 테이블 정보(tableInfo)").ok("ok").build();
    }


    @GetMapping("/test")
    public void test(@RequestHeader Map<String, String> headers){
        String token = headers.get("authorization").replaceAll("Bearer " , "");
        System.out.println("token : "+ token );
        String email = jwtProvider.getEmailFromToken(token);
        System.out.println("email : " + email);

        UserDto userDto = userService.findByEmail(UserDto.builder().email(email).build());


        if(userDto.getAdmin()){
            System.out.println(userDto.getAdmin());
        }

    }

    //헤더에 담긴 토큰으로 관리자인지 사용자인지 판닩하는 메소드
    public Boolean isAdmin(Map<String,String> headers){
        String token = headers.get("authorization").replaceAll("Bearer " , "");
        System.out.println("token : "+ token );
        String email = jwtProvider.getEmailFromToken(token);
        System.out.println("email : " + email);

        UserDto userDto = userService.findByEmail(UserDto.builder().email(email).build());


        return userDto.getAdmin();

    }

    public String getEmailFromHeader(Map<String,String> headers){
        String token = headers.get("authorization").replaceAll("Bearer " , "");
        String email = jwtProvider.getEmailFromToken(token);
        return email;
    }

    public void updateTableInfo(){


        tableInfoService.updateTableInfo();

    }


    public List<ResTableInfoDtoTerm> convertToTableInfoDtoTerm(List<TableInfoDto> tableInfoDtoList){

        List<ResTableInfoDtoTerm> resTableInfoDtoTermList = new ArrayList<ResTableInfoDtoTerm>();

        //log.info("tableInfoDtoList : " + tableInfoDtoList.toString());

        //일단 용어
        for(TableInfoDto tableInfoDto : tableInfoDtoList){
            String stdTerm = tableInfoDto.getStdTerminologyDto().getStandardTerminology();
            resTableInfoDtoTermList.add(ResTableInfoDtoTerm.builder().standardTerminology(stdTerm).build());


        }

        Set<ResTableInfoDtoTerm> setTmp = new HashSet<ResTableInfoDtoTerm>(resTableInfoDtoTermList);
        resTableInfoDtoTermList = new ArrayList<ResTableInfoDtoTerm>(setTmp);



        for(ResTableInfoDtoTerm resTableInfoDtoTerm : resTableInfoDtoTermList){

            for(TableInfoDto tableInfoDto :tableInfoDtoList){
                if(tableInfoDto.getStdTerminologyDto().getStandardTerminology().equals(resTableInfoDtoTerm.getStandardTerminology())){
                    String num = Integer.toString(tableInfoDto.getNum());
                    String tableName = tableInfoDto.getLogicalTableName();
                    Map<String,String> tmpMap = new HashMap<String, String>();
                    tmpMap.put("num" ,num);
                    tmpMap.put("tableName" , tableName);
                    resTableInfoDtoTerm.addTableList(tmpMap);
                }
            }

        }



        return resTableInfoDtoTermList;
    }

    public List<ResponseTableInfoDto> convertTableInfo(List<TableInfoDto> tableInfoDtoList){

        List<ResponseTableInfoDto> responseTableInfoDtoList = new ArrayList<ResponseTableInfoDto>();

        //num 물리명, 논리명 적재
        for(TableInfoDto tableInfoDto : tableInfoDtoList){
            responseTableInfoDtoList.add(
                    ResponseTableInfoDto.builder()
                            .num(tableInfoDto.getNum())
                            .logicalTableName(tableInfoDto.getLogicalTableName())
                            .physicalTableName(tableInfoDto.getPhysicalTableName())
                            .build());

        }


        //중복제거

        Set<ResponseTableInfoDto> tmpSet = new HashSet<ResponseTableInfoDto>(responseTableInfoDtoList);
        responseTableInfoDtoList = new ArrayList<ResponseTableInfoDto>(tmpSet);


        //stdTerminologyList 채우기
        for(TableInfoDto tableInfoDto : tableInfoDtoList){
            String physicalName = tableInfoDto.getPhysicalTableName();
            String standardTerminology = tableInfoDto.getStdTerminologyDto().getStandardTerminology();
            //List<String> tmpStrList = new ArrayList<String>();
            for(ResponseTableInfoDto res : responseTableInfoDtoList){
                if(res.getPhysicalTableName().equals(physicalName)){
                    System.out.println("공통표준용어채우기");
                    res.addStdTerminology(standardTerminology);
                }
            }


        }



        return responseTableInfoDtoList;


    }


}
