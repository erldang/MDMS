package com.example.backend.repository;

import com.example.backend.entity.TableInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableInfoRepository extends JpaRepository<TableInfo,Integer> {


    @Modifying
    @Transactional
    @Query("update TableInfo t SET t.num = ?2 where t.physicalTableName = ?1")
    void updateTableInfo(String name , int count);


    @Query("select t from TableInfo t where t.stdTerminology.standardTerminology = ?1")
    List<TableInfo> findTableInfoByStdTerminology(String standardTerminology);

    @Query("select t from TableInfo t where t.logicalTableName = ?1")
    List<TableInfo> findTableInfoByTableName(String tableName);

}
