package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.Stadium;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StadiumResponse {
    private Long stadiumId;
    private Long sportsId;
    private String stadiumName;
    private String homeTeamName;

    public static StadiumResponse fromEntity(Stadium stadium) {

        return StadiumResponse.builder()
                .stadiumId(stadium.getStadiumId())
                .sportsId(stadium.getSports().getSportsId())
                .stadiumName(stadium.getStadiumName())
                .homeTeamName(stadium.getHomeTeamName())
                .build();
    }
}
