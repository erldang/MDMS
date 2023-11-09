package com.example.backend.controller;


import com.example.backend.dto.DomainDto;
import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.StandardTerminologyDto;
import com.example.backend.entity.Domain;
import com.example.backend.service.DomainService;
import com.example.backend.service.StandardTerminologyService;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/terminology")
public class StandardTerminologyController {

    @Autowired
    StandardTerminologyService stdService;

    @Autowired
    DomainService domainService;

    @GetMapping
    public ResponseDto<Object> findAllTerminology(){

        List<StandardTerminologyDto> stdList =  stdService.findAllTerminology();

        return ResponseDto.builder().ok("ok").data(stdList).build();

    }













    //아래 쳐박아 두기
    @GetMapping("/inputData")
    public void inputData(){
        String path = "Back-end//src//main//resources//공공데이터 공통표준용어(2021.12월).xlsx";
        try{
            File file  = new File(path);

            FileInputStream fis = new FileInputStream(file);
            Workbook workBook = new XSSFWorkbook(fis);
            int rowIndex = 0;
            int colindex = 0;

            Sheet sheet = workBook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            //sheet.getPhysicalNumberOfRows()
            for(int i = 1; i < sheet.getPhysicalNumberOfRows();i++){
                Row row = sheet.getRow(i);

                StandardTerminologyDto data = new StandardTerminologyDto();
                data.setNo((int)row.getCell(0).getNumericCellValue());
                data.setDegree(formatter.formatCellValue(row.getCell(1)));
                data.setStandardTerminology(formatter.formatCellValue(row.getCell(2)));
                data.setDescription(formatter.formatCellValue(row.getCell(3)));
                data.setEnglishAbbreviation(formatter.formatCellValue(row.getCell(4)));
                String domainName = formatter.formatCellValue(row.getCell(5));

                DomainDto domainDto = domainService.findDomainByName(domainName);
                Domain domain = new Domain(domainDto);
                data.setDomain(domain);


             //   data.setTolerance(formatter.formatCellValue(row.getCell(6)));
             //   data.setSaveFormat(formatter.formatCellValue(row.getCell(7)));
             //   data.setExpressionForm(formatter.formatCellValue(row.getCell(8)));
                data.setStandardCode(formatter.formatCellValue(row.getCell(9)));
                data.setRelevantOrganization(formatter.formatCellValue(row.getCell(10)));
                data.setIsCustom(false);
                stdService.addData(data);
            }



        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
