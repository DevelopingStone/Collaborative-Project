package com.zerobase.oriticket.domain.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ticket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "SPORTS_ID")
    private Sports sports;

    @ManyToOne
    @JoinColumn(name = "STADIUM_ID")
    private Stadium stadium;

    @ManyToOne
    @JoinColumn(name = "AWAY_TEAM_ID")
    private AwayTeam awayTeam;

    private Integer quantity;
    private Integer salePrice;
    private Integer originalPrice;

    private LocalDateTime expirationAt;
    private Boolean isSuccessive;

    private String seatInfo;
    private String imgUrl;
    private String note;

}
