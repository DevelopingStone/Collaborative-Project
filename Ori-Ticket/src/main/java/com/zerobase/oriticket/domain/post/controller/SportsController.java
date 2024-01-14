package com.zerobase.oriticket.domain.post.controller;

import com.zerobase.oriticket.domain.post.dto.RegisterSportsRequest;
import com.zerobase.oriticket.domain.post.dto.SportsResponse;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.service.SportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sports")
public class SportsController {

    private final SportsService sportsService;

    @PostMapping
    public ResponseEntity<SportsResponse> register(
            @RequestBody RegisterSportsRequest request
    ){
        Sports sports = sportsService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SportsResponse.fromEntity(sports));
    }

    @GetMapping
    public ResponseEntity<SportsResponse> get(
            @RequestParam("id") Long sportsId
    ){
        Sports sports = sportsService.get(sportsId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SportsResponse.fromEntity(sports));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SportsResponse>> getAll(){

        List<Sports> sports = sportsService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(sports.stream()
                        .map(SportsResponse::fromEntity)
                        .toList());
    }

    @DeleteMapping
    public Long delete(
            @RequestParam("id") Long sportsId
    ){
        return sportsService.delete(sportsId);
    }
}
