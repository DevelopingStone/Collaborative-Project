package com.zerobase.oriticket.domain.post.controller;

import com.zerobase.oriticket.domain.post.dto.PostResponse;
import com.zerobase.oriticket.domain.post.dto.RegisterPostRequest;
import com.zerobase.oriticket.domain.post.dto.UpdateStatusToReportedPostRequest;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> register(
            @RequestBody RegisterPostRequest request
    ){
        Post salePost = postService.registerPost(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PostResponse.fromEntity(salePost));
    }

    @GetMapping
    public ResponseEntity<PostResponse> get(
            @RequestParam("id") Long salePostId
    ){
        Post salePost = postService.get(salePostId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PostResponse.fromEntity(salePost));
    }

    @DeleteMapping
    public Long delete(
            @RequestParam("id") Long salePostId
    ) {
        return postService.delete(salePostId);
    }

    @PatchMapping("/report")
    public ResponseEntity<PostResponse> updateToReported(
            @RequestBody UpdateStatusToReportedPostRequest request
    ){
        Post salePost = postService.updateToReported(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PostResponse.fromEntity(salePost));
    }
}
