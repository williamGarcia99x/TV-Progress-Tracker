package com.cognixia.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TvShow {
    private int showId;
    private String originalName;
    private Date createdAt;
}
