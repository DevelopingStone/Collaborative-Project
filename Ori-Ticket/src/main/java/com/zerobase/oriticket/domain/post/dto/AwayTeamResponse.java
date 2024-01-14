package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AwayTeamResponse {
    private Long awayTeamId;
    private Long sportsId;
    private String awayTeamName;

    public static AwayTeamResponse fromEntity(AwayTeam awayTeam) {

        return AwayTeamResponse.builder()
                .awayTeamId(awayTeam.getAwayTeamId())
                .sportsId(awayTeam.getSports().getSportsId())
                .awayTeamName(awayTeam.getAwayTeamName())
                .build();
    }
}
