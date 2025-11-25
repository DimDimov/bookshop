package com.example.buchladen.web.dto;


import com.example.buchladen.Enums.SenderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    Long id;
    private String text;
    private String senderName;
    private boolean fromAdmin;
    private String createdAt;
    private SenderType senderType;
}
