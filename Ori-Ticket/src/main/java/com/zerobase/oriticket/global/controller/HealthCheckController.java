package com.zerobase.oriticket.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public ResponseEntity<Void> heathCheck(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
