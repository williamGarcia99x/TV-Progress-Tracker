package com.cognixia;

import com.cognixia.model.UserTvTracker;
import com.cognixia.model.WatchStatus;
import com.cognixia.service.UserTvTrackerService;
import com.cognixia.util.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);


    }




}
