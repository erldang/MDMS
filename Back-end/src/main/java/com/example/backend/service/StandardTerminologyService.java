package com.example.backend.service;

import com.example.backend.dto.StandardTerminologyDto;
import com.example.backend.entity.StandardTerminology;
import com.example.backend.repository.StandardTerminologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StandardTerminologyService {

    @Autowired
    StandardTerminologyRepository stdRepository;


    public List<StandardTerminologyDto> findAllTerminology(){

        List<StandardTerminology> stdList = stdRepository.findAll();
        List<StandardTerminologyDto> stdDtoList = new ArrayList<StandardTerminologyDto>();

        for(StandardTerminology std : stdList){
            stdDtoList.add(new StandardTerminologyDto(std));
        }

        return stdDtoList;
    }

    public void addData(StandardTerminologyDto stdDto){

        stdRepository.save(new StandardTerminology(stdDto));

    }


}
