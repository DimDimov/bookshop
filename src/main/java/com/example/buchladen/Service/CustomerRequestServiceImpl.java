package com.example.buchladen.Service;


import com.example.buchladen.Enums.RequestStatus;
import com.example.buchladen.Enums.SenderType;
import com.example.buchladen.Model.CustomerRequest;
import com.example.buchladen.Model.Order;
import com.example.buchladen.Model.RequestMessage;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.CustomerRequestRepository;
import com.example.buchladen.Repositories.RequestMessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerRequestServiceImpl implements CustomerRequestService {

    private final CustomerRequestRepository repo;

    private final RequestMessageRepository msgRepo;

    public CustomerRequestServiceImpl(CustomerRequestRepository repo, RequestMessageRepository msgRepo) {
        this.repo = repo;
        this.msgRepo = msgRepo;
    }

    @Override
    public Optional<CustomerRequest> findByCustomerAndOrder(User user, Order order) {
        return repo.findByCustomerAndOrder(user, order);
    }

    @Override
    public CustomerRequest findById(Long id) {
        return repo.findById(id).orElseThrow(()-> new RuntimeException("Anfrage nicht gefunden"));
    }

    @Override
    public List<CustomerRequest> findByCustomer(User user) {
        return repo.findByCustomer(user);
    }

    @Override
    public void save(CustomerRequest request) {
        repo.save(request);
    }

    @Override
    public List<CustomerRequest> getAllRequests() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void updateStatus(Long id, RequestStatus status) {
        CustomerRequest request = repo.findById(id).orElseThrow();
        request.setStatus(status);
    }

    @Override
    public void saveAdminMessage(Long id, String answer, User admin) {

        CustomerRequest request = repo.findById(id).orElseThrow();

        RequestMessage msg = new RequestMessage();
        msg.setRequest(request);
        msg.setReadByUser(false);
        msg.setSender(admin);
        msg.setText(answer);
        msg.setCreatedAt(LocalDateTime.now());
        msg.setSenderType(SenderType.ADMIN);
        request.addMessage(msg);

        repo.save(request);
    }

    @Transactional
    public void markMessageAsRead(Long userId, Long requestId) {
        CustomerRequest request = repo.findById(requestId)
                .orElseThrow();

        List<RequestMessage> toUpdate = request.getMessages().stream()
                .filter(msg -> {

                    Long senderId = msg.getSender().getId();
                    return !msg.isReadByUser() && !senderId.equals(userId);
                })
                .peek(msg -> msg.setReadByUser(true))
                .collect(Collectors.toList());
        if (!toUpdate.isEmpty()) {

            msgRepo.saveAll(toUpdate);
        }
    }
}
