package com.example.backend.entity;

import com.example.backend.dto.HistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(nullable = false)
    private String tableName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Date registrationDate;

    public History(HistoryDto historyDto){
        this.no = historyDto.getNo();
        this.tableName = historyDto.getTableName();
        this.email = historyDto.getEmail();
        this.registrationDate = historyDto.getRegistrationDate();
    }

}
