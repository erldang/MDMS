package com.example.backend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
public class ResponseTableInfoDto {

    private String logicalTableName;
    private String physicalTableName;
    private int num;
    private List<String> stdTerminologyList;

    public void addStdTerminology(String standardTerminology){
        if(stdTerminologyList == null){
            stdTerminologyList = new ArrayList<String>();
        }


        stdTerminologyList.add(standardTerminology);
    }


}
