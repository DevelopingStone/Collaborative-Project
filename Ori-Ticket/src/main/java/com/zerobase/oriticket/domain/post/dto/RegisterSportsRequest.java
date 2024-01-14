package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.Sports;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSportsRequest {

    private String sportsName;

    public Sports toEntity(){
        return Sports.builder()
                .sportsName(this.sportsName)
                .build();
    }
}
