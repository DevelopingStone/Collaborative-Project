package com.zerobase.oriticket.domain.members.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class AdminRequest {

    private String email;
    private String name;
    private String nickname;

}



