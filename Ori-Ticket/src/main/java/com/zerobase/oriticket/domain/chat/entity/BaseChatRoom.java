package com.zerobase.oriticket.domain.chat.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class BaseChatRoom {

    @CreatedDate
    private LocalDateTime createdAt;

}
