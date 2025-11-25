package com.example.buchladen.Service;


import com.example.buchladen.Model.RequestMessage;
import com.example.buchladen.Repositories.RequestMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestMessageServiceImpl implements RequestMessageService{


    private final RequestMessageRepository requestMessageRepository;

    public RequestMessageServiceImpl(RequestMessageRepository requestMessageRepository) {
        this.requestMessageRepository = requestMessageRepository;
    }

    @Override
    public void saveAll(List<RequestMessage> toUpdate) {
        requestMessageRepository.saveAll(toUpdate);
    }
}
