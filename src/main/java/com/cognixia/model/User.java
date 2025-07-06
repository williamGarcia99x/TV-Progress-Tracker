package com.cognixia.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private int userId;
    private String username;
    private String password_hash;
    private LocalDate createdAt;

    public User(String username, String password_hash) {
        this.username = username;
        this.password_hash = password_hash;
    }
}


