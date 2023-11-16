package com.example.backend.dto;

import com.example.backend.entity.StandardTerminology;
import com.example.backend.entity.TableInfo;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfoDto {

    private int no;
    private String logicalTableName;
    private String physicalTableName;
    private int num;
    private StandardTerminologyDto stdTerminologyDto;


    public TableInfoDto(TableInfo tableInfo){
        this.no = tableInfo.getNo();
        this.logicalTableName = tableInfo.getLogicalTableName();
        this.physicalTableName = tableInfo.getPhysicalTableName();
        this.stdTerminologyDto = new StandardTerminologyDto(tableInfo.getStdTerminology());
        this.num = tableInfo.getNum();

    }


}
