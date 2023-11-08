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
    private String classificationName; //분류명
    private String domain; //도메인명

    @Column(length = 1000)
    private String description; //설명
    private String dataType; //데이터 타입명
    private String dataLength; //데이터 길이
    private String decimalPointLength; //데이터 소수점 길이
    private String saveFormat; //저장 형식
    private String expressionForm; // 표현 형식
    private String unitName; //단위명
    private String tolerance; //허용값
    private String degree; //재정차수

    public Domain(DomainDto domainDto){
        this.no = domainDto.getNo();
        this.domain = domainDto.getDomain();
        this.classificationName = domainDto.getClassificationName();
        this.description = domainDto.getDescription();
        this.dataType = domainDto.getDataType();
        this.dataLength = domainDto.getDataLength();
        this.decimalPointLength = domainDto.getDecimalPointLength();
        this.saveFormat = domainDto.getSaveFormat();
        this.expressionForm = domainDto.getExpressionForm();
        this.unitName = domainDto.getUnitName();
        this.tolerance = domainDto.getTolerance();
        this.degree = domainDto.getDegree();
    }



}