package com.example.backend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//tableInfo에서 용어를 기준으로 요청할떄 사용하는 DTO
public class ResTableInfoDtoTerm {

    private String standardTerminology;
    private List<Map<String,String>> tableList;

    public void addTableList(Map<String,String> para){
        if(tableList == null){
            tableList = new ArrayList<Map<String , String>>();
        }

        tableList.add(para);
    }

}
