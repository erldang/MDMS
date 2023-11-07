package com.example.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;

@Data
@Getter
@Mapper
@AllArgsConstructor
@NoArgsConstructor
public class TableDto {
    private String email;
    private String tableName;
    private String query;
}
