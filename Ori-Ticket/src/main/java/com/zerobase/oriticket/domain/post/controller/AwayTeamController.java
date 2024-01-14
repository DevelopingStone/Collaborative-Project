package com.zerobase.oriticket.domain.post.controller;

import com.zerobase.oriticket.domain.post.dto.AwayTeamResponse;
import com.zerobase.oriticket.domain.post.dto.RegisterAwayTeamRequest;
import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.service.AwayTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/awayteam")
public class AwayTeamController {

    private final AwayTeamService awayTeamService;

    @PostMapping
    public ResponseEntity<AwayTeamResponse> register(
            @RequestBody RegisterAwayTeamRequest request
    ){

        AwayTeam awayTeam = awayTeamService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(AwayTeamResponse.fromEntity(awayTeam));
    }

    @GetMapping
    public ResponseEntity<AwayTeamResponse> get(
            @RequestParam("id") Long awayTeamId
    ){
        AwayTeam awayTeam = awayTeamService.get(awayTeamId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(AwayTeamResponse.fromEntity(awayTeam));
    }

    @GetMapping("/list")
    public ResponseEntity<List<AwayTeamResponse>> getAll(){

        List<AwayTeam> awayTeams = awayTeamService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(awayTeams.stream()
                        .map(AwayTeamResponse::fromEntity)
                        .toList());
    }

    @GetMapping("/sports")
    public ResponseEntity<List<AwayTeamResponse>> getBySportId(
            @RequestParam("id") Long sportsId
    ){
        List<AwayTeam> awayTeams = awayTeamService.getBySportsId(sportsId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        awayTeams.stream()
                        .map(AwayTeamResponse::fromEntity)
                        .toList()
                );
    }

    @DeleteMapping
    public Long delete(
            @RequestParam("id") Long awayTeamId
    ){
        return awayTeamService.delete(awayTeamId);
    }
}
