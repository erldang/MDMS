package com.example.backend.entity;

import com.example.backend.dto.DomainDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int no;
    private String degree; //재정차수
    private String groupName; //공통표준도메인 그룹명
    @Column(length = 1000)
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

    public Domain(DomainDto domainDto){
        this.no = domainDto.getNo();
        this.groupName = domainDto.getGroupName();
        this.name = domainDto.getName();
        this.classificationName = domainDto.getClassificationName();
        this.description = domainDto.getDescription();
        this.dataType = domainDto.getDataType();
        this.dataLength = domainDto.getDataLength();
        this.decimalPointLength = domainDto.getDecimalPointLength();
        this.saveFormat = domainDto.getSaveFormat();
        this.expressionForm = domainDto.getExpressionForm();
        this.unit = domainDto.getUnit();
        this.tolerance = domainDto.getTolerance();
        this.degree = domainDto.getDegree();
    }



}