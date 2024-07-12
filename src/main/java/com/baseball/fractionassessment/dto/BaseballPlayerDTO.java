package com.baseball.fractionassessment.dto;

import com.baseball.fractionassessment.model.BaseballPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseballPlayerDTO {
    private Long id;
    private String rank;
    private String player;
    private String ageThatYear;
    private String bats;
    private int hits;
    private int year;
    private String description;

    public static BaseballPlayer to(BaseballPlayerDTO dto) {
        return BaseballPlayer.builder()
                .id(dto.getId())
                .rank(dto.getRank())
                .player(dto.getPlayer())
                .ageThatYear(dto.getAgeThatYear())
                .bats(dto.getBats())
                .hits(dto.getHits())
                .year(dto.getYear())
                .build();
    }

    public static BaseballPlayerDTO from(BaseballPlayer player) {
        return BaseballPlayerDTO.builder()
                .id(player.getId())
                .rank(player.getRank())
                .player(player.getPlayer())
                .ageThatYear(player.getAgeThatYear())
                .bats(player.getBats())
                .hits(player.getHits())
                .year(player.getYear())
                .build();
    }

}
