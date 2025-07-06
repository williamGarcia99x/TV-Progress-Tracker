package com.cognixia.model;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor

@ToString
public class TvShow {
    private int showId;
    private String name;
    private String originalName;
    private String posterPath;
    private LocalDate createdAt;
}
