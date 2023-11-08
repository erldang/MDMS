package com.example.backend.entity;


import com.example.backend.dto.StandardTerminologyDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


//공통표준용아
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandardTerminology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String degree;
    private String description;
    private String standardTerminology;
    private String englishAbbreviation;
    private String domain;
    private String tolerance;
    private String saveFormat;
    private String expressionForm;
    private String standardCode;
    private String relevantOrganization;

    public StandardTerminology(StandardTerminologyDto std){
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
