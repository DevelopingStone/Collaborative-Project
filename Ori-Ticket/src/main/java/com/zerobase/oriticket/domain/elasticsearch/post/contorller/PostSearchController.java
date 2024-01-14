package com.zerobase.oriticket.domain.elasticsearch.post.contorller;

import com.zerobase.oriticket.domain.elasticsearch.post.dto.PostSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostSearchController {

    private final PostSearchService postSearchService;

    @GetMapping("/search")
    public ResponseEntity<Page<PostSearchResponse>> search(
            @RequestParam(name = "value", required = false) String value,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<PostSearchDocument> postSearchDocuments =
                postSearchService.search(value, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(postSearchDocuments.map(PostSearchResponse::fromEntity));
    }
}
