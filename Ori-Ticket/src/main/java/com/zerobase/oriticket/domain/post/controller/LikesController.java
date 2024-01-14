package com.zerobase.oriticket.domain.post.controller;

import com.zerobase.oriticket.domain.post.dto.LikesResponse;
import com.zerobase.oriticket.domain.post.dto.RegisterLikesRequest;
import com.zerobase.oriticket.domain.post.entity.Likes;
import com.zerobase.oriticket.domain.post.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/posts/{salePostId}/likes")
    public ResponseEntity<LikesResponse> register(
            @PathVariable("salePostId") Long salePostId,
            @RequestBody RegisterLikesRequest request
    ){
        Likes likes = likesService.register(salePostId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(LikesResponse.fromEntity(likes));
    }

    @DeleteMapping("/posts/{salePostId}/likes")
    public ResponseEntity<Long> delete(
            @PathVariable("salePostId") Long salePostId,
            @RequestParam("memberId") Long memberId
    ){
        Long likesId = likesService.delete(salePostId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(likesId);
    }

    @GetMapping("/members/{memberId}/likes")
    public ResponseEntity<List<LikesResponse>> get(
            @PathVariable("memberId") Long memberId
    ){
        List<Likes> likesList = likesService.get(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(likesList.stream()
                        .map(LikesResponse::fromEntity)
                        .toList());

    }
}
