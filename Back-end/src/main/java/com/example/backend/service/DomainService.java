package com.example.backend.service;

import com.example.backend.dto.DomainDto;
import com.example.backend.dto.StandardTerminologyDto;
import com.example.backend.entity.Domain;
import com.example.backend.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomainService {

    @Autowired
    DomainRepository domainRepository;


    public List<DomainDto> findAllDomain(){

        List<Domain> domainList = domainRepository.findAll();
        List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
        for(Domain domain : domainList){
            domainDtoList.add(new DomainDto(domain));
        }

        return domainDtoList;
    }


    public void addData(DomainDto domainDto) {

        domainRepository.save(new Domain(domainDto));
    }

    public DomainDto findDomainByName(String name){
        System.out.println("domain Name : " + name);
        Domain domain = domainRepository.findDomainByName(name);
        System.out.println("findDomainByName : " + domain.toString());
        return new DomainDto(domain);



    }

}
