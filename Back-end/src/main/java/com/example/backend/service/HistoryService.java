package com.example.backend.service;

import com.example.backend.dto.HistoryDto;
import com.example.backend.entity.History;
import com.example.backend.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    public void addHistory(HistoryDto historyDto){

        historyRepository.save(new History(historyDto));

    }

    public List<HistoryDto> findAllHistory(){

        List<History> historyList = historyRepository.findAll();
        List<HistoryDto> historyDtoList = new ArrayList<HistoryDto>();
        for(History history : historyList){
            historyDtoList.add(new HistoryDto(history));
        }

        return historyDtoList;
    }

}
