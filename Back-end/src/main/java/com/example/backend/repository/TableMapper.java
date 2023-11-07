package com.example.backend.repository;

import com.example.backend.dto.TableDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableMapper {

    void createTable(String query);
}
