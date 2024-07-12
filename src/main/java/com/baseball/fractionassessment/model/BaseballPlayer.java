package com.baseball.fractionassessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "baseball")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseballPlayer {
    @Id
    private Long id;
    @JsonProperty("Rank")
    private String rank;
    @JsonProperty("Player")
    private String player;
    @JsonProperty("AgeThatYear")
    private String ageThatYear;
    @JsonProperty("Bats")
    private String bats;
    @JsonProperty("Hits")
    private int hits;
    @JsonProperty("Year")
    private int year;


}
