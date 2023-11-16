package com.example.backend.dto;


import com.example.backend.entity.Domain;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DomainDto {

    private int no;
    private String degree; //재정차수
    private String groupName; //공통표준도메인 그룹명
    private String classificationName; //분류명
    private String name; //도메인명
    private String description; //설명
    private String dataType; //데이터 타입명
    private String dataLength; //데이터 길이
    private String decimalPointLength; //데이터 소수점 길이
    private String saveFormat; //저장 형식
    private String expressionForm; // 표현 형식
    private String unit; //단위명
    private String tolerance; //허용값


    public DomainDto(Domain domain){
        this.no = domain.getNo();
        this.degree = domain.getDegree();
        this.groupName = domain.getGroupName();
        this.classificationName = domain.getClassificationName();
        this.name = domain.getName();
        this.description = domain.getDescription();
        this.dataType = domain.getDataType();
        this.dataLength = domain.getDataLength();
        this.decimalPointLength = domain.getDecimalPointLength();
        this.saveFormat = domain.getSaveFormat();
        this.expressionForm = domain.getExpressionForm();
        this.unit = domain.getUnit();
        this.tolerance = domain.getTolerance();
    }

}
