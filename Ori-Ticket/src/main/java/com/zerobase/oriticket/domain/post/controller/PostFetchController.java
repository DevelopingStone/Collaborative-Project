package com.zerobase.oriticket.domain.post.controller;

import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.dto.PostResponse;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.post.service.PostFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostFetchController {
    private final PostFetchService postFetchService;

    @GetMapping("/members/{memberId}/posts")
    public ResponseEntity<List<PostResponse>> get(
            @PathVariable("memberId") Long memberId,
            @RequestParam("status") List<String> statusList
    ){
        List<SaleStatus> saleStatusList = statusList.stream()
                .map(String::toUpperCase)
                .map(SaleStatus::valueOf)
                .toList();

        List<Post> postList = postFetchService.get(memberId, saleStatusList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(postList.stream()
                        .map(PostResponse::fromEntity)
                        .toList());
    }
}
