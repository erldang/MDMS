package com.example.backend.repository;

import com.example.backend.dto.TableDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface TableMapper {

    void createTable(String query);

    List<HashMap<String,String>> showTables();

    List<HashMap<String,String>> findTableByPhysicalName(String physicalName);

    String findPhysicalNameByLogicalName(String logicalName);

    Integer countTable(String tableName);

    HashMap<String, BigDecimal>  getTableSize(String tableName);
}
