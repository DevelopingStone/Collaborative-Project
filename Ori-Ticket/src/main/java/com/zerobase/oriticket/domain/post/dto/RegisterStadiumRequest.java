package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.entity.Stadium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStadiumRequest {

    private Long sportsId;
    private String stadiumName;
    private String homeTeamName;

    public Stadium toEntity(Sports sports){
        return Stadium.builder()
                .sports(sports)
                .stadiumName(this.stadiumName)
                .homeTeamName(this.homeTeamName)
                .build();
    }

}
