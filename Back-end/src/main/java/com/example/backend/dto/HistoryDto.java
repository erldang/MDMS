package com.example.backend.dto;


import com.example.backend.entity.History;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {

    private int no;

    private String physicalTableName;
    private String logicalTableName;
    private String email;

    private Date registrationDate;

    public HistoryDto(History history){
        this.no = history.getNo();
        this.physicalTableName = history.getPhysicalTableName();
        this.logicalTableName = history.getLogicalTableName();
        this.email = history.getEmail();
        this.registrationDate = history.getRegistrationDate();
    }
}
