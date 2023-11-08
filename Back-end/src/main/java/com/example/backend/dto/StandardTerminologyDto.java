package com.example.backend.dto;

import com.example.backend.entity.StandardTerminology;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StandardTerminologyDto {

    private int no; //번호
    private String degree; //차수
    private String standardTerminology; //공통표준용어명
    private String description; // 공통표준용어설명
    private String englishAbbreviation; // 공통표준용어약어명
    private String domain; // 공통표준도메인명
    private String tolerance; // 허용값
    private String saveFormat; // 저장형식
    private String expressionForm; // 표현형식
    private String standardCode; // 행정표준코드
    private String relevantOrganization; // 소관기관명


    //공통표준용어
    public StandardTerminologyDto(StandardTerminology std){
        this.no = std.getNo();
        this.degree = std.getDegree();
        this.standardTerminology = std.getStandardTerminology();
        this.description = std.getDescription();
        this.englishAbbreviation = std.getEnglishAbbreviation();
        this.domain = std.getDomain();
        this.tolerance = std.getTolerance();
        this.saveFormat = std.getSaveFormat();
        this.expressionForm = std.getExpressionForm();
        this.standardCode = std.getStandardCode();
        this.relevantOrganization = std.getRelevantOrganization();
    }
}
