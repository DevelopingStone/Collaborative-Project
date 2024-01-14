package com.zerobase.oriticket.domain.post.dto;

import com.zerobase.oriticket.domain.post.entity.Likes;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikesResponse {
    private Long likesId;
    private Long memberId;
    private PostResponse salePost;

    public static LikesResponse fromEntity(Likes likes){

        return LikesResponse.builder()
                .likesId(likes.getLikesId())
                .memberId(likes.getMember().getMemberId())
                .salePost(PostResponse.fromEntity(likes.getSalePost()))
                .build();
    }
}
