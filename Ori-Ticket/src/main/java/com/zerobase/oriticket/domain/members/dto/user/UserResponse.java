package com.zerobase.oriticket.domain.members.dto.user;

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
public class UserResponse {

    private Boolean existsByEmail;
    private String email;
    private Long id;

    public UserResponse toEntity(UserRequest userRequest) {
        return UserResponse.builder()
                .existsByEmail(userRequest.getExistsByEmail())
                .email(userRequest.getEmail())
                .id(userRequest.getId())
                .build();
    }

}
