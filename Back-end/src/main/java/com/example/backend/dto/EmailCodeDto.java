package com.example.backend.dto;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailCodeDto {

    private String email;
    private String code;

}
