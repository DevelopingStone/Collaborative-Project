package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.Ticket;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResponse {

    private String sportsName;
    private String stadiumName;
    private String homeTeamName;
    private String awayTeamName;

    private int quantity;
    private int salePrice;
    private int originalPrice;

    private LocalDateTime expirationAt;
    private Boolean isSuccessive;

    private String seatInfo;
    private String imgUrl;
    private String note;

    public static TicketResponse fromEntity(Ticket ticket) {
        return TicketResponse.builder()
                .sportsName(ticket.getSports().getSportsName())
                .stadiumName(ticket.getStadium().getStadiumName())
                .homeTeamName(ticket.getStadium().getHomeTeamName())
                .awayTeamName(ticket.getAwayTeam().getAwayTeamName())
                .quantity(ticket.getQuantity())
                .salePrice(ticket.getSalePrice())
                .originalPrice(ticket.getOriginalPrice())
                .expirationAt(ticket.getExpirationAt())
                .isSuccessive(ticket.getIsSuccessive())
                .seatInfo(ticket.getSeatInfo())
                .imgUrl(ticket.getImgUrl())
                .note(ticket.getNote())
                .build();
    }

}
