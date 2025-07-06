package com.cognixia.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowSummaryDTO {
    private int id; // TMDB ID
    private String name; // Localized title (you can alias original_name here if needed)
    private String original_name;
    private String poster_path; // Store this in DB
}
