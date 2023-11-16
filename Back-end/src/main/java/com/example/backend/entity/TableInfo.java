package com.example.backend.entity;


import com.example.backend.dto.TableInfoDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TableInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String logicalTableName;
    private String physicalTableName;
    private int num;

    @ManyToOne
    @JoinColumn(name = "standardTerminology")
    private StandardTerminology stdTerminology;


    public TableInfo(TableInfoDto tableInfodto){
        this.no = tableInfodto.getNo();
        this.logicalTableName = tableInfodto.getLogicalTableName();
        this.physicalTableName = tableInfodto.getPhysicalTableName();
        this.stdTerminology = new StandardTerminology(tableInfodto.getStdTerminologyDto());
        this.num = tableInfodto.getNum();

    }

}
