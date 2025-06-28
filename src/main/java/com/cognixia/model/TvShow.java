package com.cognixia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class TvShow {
    private int showId;
    private String originalName;
    private Date created_at;
}
