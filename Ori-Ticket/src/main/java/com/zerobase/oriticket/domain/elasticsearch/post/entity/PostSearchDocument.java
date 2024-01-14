package com.zerobase.oriticket.domain.elasticsearch.post.entity;

import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "post")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostSearchDocument {

    @Id
    private Long salePostId;

    private String memberName;
    private String sportsName;
    private String stadiumName;
    private String homeTeamName;
    private String awayTeamName;

    private Integer quantity;
    private Integer salePrice;
    private Integer originalPrice;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime expirationAt;
    private Boolean isSuccessive;

    private String seatInfo;
    private String imgUrl;
    private String note;

    private SaleStatus saleStatus;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;

    public static PostSearchDocument fromEntity(Post post){
        return PostSearchDocument.builder()
                .salePostId(post.getSalePostId())
                .memberName(post.getMember().getNickname())
                .sportsName(post.getTicket().getSports().getSportsName())
                .stadiumName(post.getTicket().getStadium().getStadiumName())
                .homeTeamName(post.getTicket().getStadium().getHomeTeamName())
                .awayTeamName(post.getTicket().getAwayTeam().getAwayTeamName())
                .quantity(post.getTicket().getQuantity())
                .salePrice(post.getTicket().getSalePrice())
                .originalPrice(post.getTicket().getOriginalPrice())
                .expirationAt(post.getTicket().getExpirationAt())
                .isSuccessive(post.getTicket().getIsSuccessive())
                .seatInfo(post.getTicket().getSeatInfo())
                .imgUrl(post.getTicket().getImgUrl())
                .note(post.getTicket().getNote())
                .saleStatus(post.getSaleStatus())
                .createdAt(post.getCreatedAt())
                .build();

    }
}
