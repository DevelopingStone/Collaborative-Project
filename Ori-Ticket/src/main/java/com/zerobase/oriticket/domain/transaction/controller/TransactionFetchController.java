package com.zerobase.oriticket.domain.transaction.controller;

import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.dto.TransactionResponse;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.service.TransactionFetchService;
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
public class TransactionFetchController {

    private final TransactionFetchService transactionFetchService;

    @GetMapping("/members/{memberId}/transactions")
    public ResponseEntity<List<TransactionResponse>> get(
            @PathVariable("memberId") Long memberId,
            @RequestParam("status") List<String> statusList
    ){
        List<TransactionStatus> transactionStatusList = statusList.stream()
                .map(String::toUpperCase)
                .map(TransactionStatus::valueOf)
                .toList();

        List<Transaction> transactions = transactionFetchService.get(memberId, transactionStatusList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(transactions.stream()
                        .map(TransactionResponse::fromEntity)
                        .toList());
    }
}
