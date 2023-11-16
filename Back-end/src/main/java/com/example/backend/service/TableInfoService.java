package com.example.backend.service;

import com.example.backend.dto.TableInfoDto;
import com.example.backend.entity.TableInfo;
import com.example.backend.repository.TableInfoRepository;
import com.example.backend.repository.TableMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TableInfoService {

    @Autowired
    TableInfoRepository tableInfoRepository;

    @Autowired
    TableService tableService;
    public void addTableInfo(TableInfoDto tableInfoDto){

        TableInfo tableInfo = new TableInfo(tableInfoDto);

        tableInfoRepository.save(tableInfo);

    }


    public List<TableInfoDto> findAllTableInfo() {

        List<TableInfo> tableInfoList = tableInfoRepository.findAll();
        List<TableInfoDto> tableInfoDtoList = new ArrayList<TableInfoDto>();

        for(TableInfo tableInfo : tableInfoList){
            tableInfoDtoList.add(new TableInfoDto(tableInfo));
        }

        return tableInfoDtoList;


    }

    public void updateTableInfo() {

        //사용자 이력 가져오기
        List<TableInfo> tableInfoList = tableInfoRepository.findAll();
        List<String> tableName = new ArrayList<String>();

        //물리명만 남기기
        for(TableInfo tableInfo : tableInfoList){

            String physicalTableName = tableInfo.getPhysicalTableName();
            tableName.add(physicalTableName);

        }

        //물리명 중복 제거 하기
        Set<String> set  = new HashSet<String>(tableName);
        List<String> newTableNameList = new ArrayList<String>(set);


        for(String name : newTableNameList ){
            int count = tableService.countTable(name);
            tableInfoRepository.updateTableInfo(name , count);

        }

    }

    public List<TableInfoDto> findTableInfoByStdTerminology(String standardTerminology) {

        List<TableInfo> tableInfoList = tableInfoRepository.findTableInfoByStdTerminology(standardTerminology);
        List<TableInfoDto> tableInfoDtoList = new ArrayList<TableInfoDto>();

        for(TableInfo tableInfo : tableInfoList){

            tableInfoDtoList.add(new TableInfoDto(tableInfo));

        }

        return tableInfoDtoList;

    }

    public List<TableInfoDto> findTableInfoByTableName(String tableName) {

        List<TableInfo> tableInfoList = tableInfoRepository.findTableInfoByTableName(tableName);
        List<TableInfoDto> tableInfoDtoList = new ArrayList<TableInfoDto>();

        for(TableInfo tableInfo : tableInfoList){

            tableInfoDtoList.add(new TableInfoDto(tableInfo));

        }

        return tableInfoDtoList;

    }
}
