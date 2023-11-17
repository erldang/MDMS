package com.example.backend.service;


import com.example.backend.dto.TableDto;
import com.example.backend.repository.TableMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class TableService {


    @Autowired
    TableMapper tableMapper;


    public String createTable(TableDto tableDto){

        String query = tableDto.getQuery();
        String message = "테이블 생성 완료";
        try{
            tableMapper.createTable(query);

        }catch (Exception e) {
            //message = "테이블 물리 이름 중복";
            e.printStackTrace();
            return "중복";
        }


        return message;


    }

    public List<HashMap<String,String>> showTables(){

        List<HashMap<String,String>> tables = tableMapper.showTables();


        return tables;
    }

    public List<HashMap<String,String>> findTableByPhysicalName(String physicalName){

        List<HashMap<String,String>> table = tableMapper.findTableByPhysicalName(physicalName);
        return table;
    }


    public List<HashMap<String, String>> findTableByLogicalName(String logicalName) {
        String physicalName = tableMapper.findPhysicalNameByLogicalName(logicalName);
        //List<HashMap<String,String>> table = tableMapper.findTableByLogicalName(logicalName);
        List<HashMap<String,String>> table = tableMapper.findTableByPhysicalName(physicalName);
        return table;
    }

    public int countTable(String tableName){
        return tableMapper.countTable(tableName);
    }


    public BigDecimal getTableSize(String tableName) {

        HashMap<String,BigDecimal> map = tableMapper.getTableSize(tableName);

        BigDecimal tableSize = map.get("Size");

        return tableSize;
    }

    public void dropTable(String physicalTableName) {

        tableMapper.dropTable(physicalTableName);
    }
}
