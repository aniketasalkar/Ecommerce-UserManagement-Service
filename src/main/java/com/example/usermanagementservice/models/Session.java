package com.example.usermanagementservice.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Session extends BaseModel{

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @Column(nullable = false, length = 512)
    private String accessToken;

    @Column(nullable = false, length = 512)
    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionState sessionState;
}
