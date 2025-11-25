package com.example.buchladen.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class RequestMessageDto {

    private String sendername;
    private String text;
    private String createdAt;
    private boolean fromAdmin;



}
