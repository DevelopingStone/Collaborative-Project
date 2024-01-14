package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.Sports;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SportsResponse {
    private Long sportsId;
    private String sportsName;

    public static SportsResponse fromEntity(Sports sports) {

        return SportsResponse.builder()
                .sportsId(sports.getSportsId())
                .sportsName(sports.getSportsName())
                .build();
    }
}
