package com.zerobase.oriticket.domain.elasticsearch.post.dto;

import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostSearchResponse {

    private Long salePostId;
    private String memberName;
    private String sportsName;
    private String stadiumName;
    private String homeTeamName;
    private String awayTeamName;
    private Integer quantity;
    private Integer salePrice;
    private Integer originalPrice;
    private LocalDateTime expirationAt;
    private Boolean isSuccessive;
    private String seatInfo;
    private String imgUrl;
    private String note;
    private String saleStatus;
    private LocalDateTime createdAt;

    public static PostSearchResponse fromEntity(PostSearchDocument post){

        return PostSearchResponse.builder()
                .salePostId(post.getSalePostId())
                .memberName(post.getMemberName())
                .sportsName(post.getSportsName())
                .stadiumName(post.getStadiumName())
                .homeTeamName(post.getHomeTeamName())
                .awayTeamName(post.getAwayTeamName())
                .quantity(post.getQuantity())
                .salePrice(post.getSalePrice())
                .originalPrice(post.getOriginalPrice())
                .expirationAt(post.getExpirationAt())
                .isSuccessive(post.getIsSuccessive())
                .seatInfo(post.getSeatInfo())
                .imgUrl(post.getImgUrl())
                .note(post.getNote())
                .saleStatus(post.getSaleStatus().toString())
                .createdAt(post.getCreatedAt())
                .build();

    }
}
