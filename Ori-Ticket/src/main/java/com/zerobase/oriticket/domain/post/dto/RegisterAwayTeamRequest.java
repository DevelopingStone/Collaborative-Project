package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.entity.Sports;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAwayTeamRequest {

    private Long sportsId;
    private String awayTeamName;

    public AwayTeam toEntity(Sports sports){
        return AwayTeam.builder()
                .sports(sports)
                .awayTeamName(this.awayTeamName)
                .build();
    }
}
