package com.example.buchladen.Repositories;

import com.example.buchladen.Model.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestMessageRepository extends JpaRepository<RequestMessage, Long> {

}
