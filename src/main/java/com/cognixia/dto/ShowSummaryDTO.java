package com.cognixia.dto;

import lombok.*;



//Class used when returning show summary information to the client
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowSummaryDTO {
    private Integer id; // TMDB ID
    private String name; // Localized title (you can alias original_name here if needed)
    private String original_name;
    private String poster_path; // Store this in DB
}
