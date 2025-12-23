package com.example.buchladen.Model;


import com.example.buchladen.Enums.RequestReason;
import com.example.buchladen.Enums.RequestStatus;
import com.example.buchladen.Enums.RequestType;
import com.example.buchladen.Enums.SenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CustomerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private User customer;
    @ManyToOne private Order order;

    @Enumerated(EnumType.STRING)
    private RequestType type;
    @Enumerated(EnumType.STRING)
    private RequestReason reason;
    private String message;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
  /*  @Enumerated(EnumType.STRING)
    private SenderType senderType;*/
    @Transient
    private String bookSummary;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestMessage> messages = new ArrayList<>();

    public void addMessage(RequestMessage msg) {
        messages.add(msg);
        msg.setRequest(this);
    }
    @ManyToOne
    private Book book;

    private boolean hasUnreadMessage;
}
