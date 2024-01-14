package com.zerobase.oriticket.domain.post.controller;

import com.zerobase.oriticket.domain.post.dto.RegisterStadiumRequest;
import com.zerobase.oriticket.domain.post.dto.StadiumResponse;
import com.zerobase.oriticket.domain.post.entity.Stadium;
import com.zerobase.oriticket.domain.post.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stadium")
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumResponse> register(
            @RequestBody RegisterStadiumRequest request
    ){
        Stadium stadium = stadiumService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(StadiumResponse.fromEntity(stadium));
    }

    @GetMapping
    public ResponseEntity<StadiumResponse> get(
            @RequestParam("id") Long stadiumId
    ){
        Stadium stadium = stadiumService.get(stadiumId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(StadiumResponse.fromEntity(stadium));
    }

    @GetMapping("/list")
    public ResponseEntity<List<StadiumResponse>> getAll(){

        List<Stadium> stadiums = stadiumService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(stadiums.stream()
                        .map(StadiumResponse::fromEntity)
                        .toList());
    }

    @GetMapping("/sports")
    public ResponseEntity<List<StadiumResponse>> getBySportId(
            @RequestParam("id") Long sportsId
    ){
        List<Stadium> stadiums = stadiumService.getBySportsId(sportsId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(stadiums.stream()
                        .map(StadiumResponse::fromEntity)
                        .toList());
    }

    @DeleteMapping
    public Long delete(
            @RequestParam("id") Long stadiumId
    ){
        return stadiumService.delete(stadiumId);
    }
}
