package com.cognixia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class Session {

    private String token;
    private Integer userId;
    private LocalDateTime expiresAt;
}
