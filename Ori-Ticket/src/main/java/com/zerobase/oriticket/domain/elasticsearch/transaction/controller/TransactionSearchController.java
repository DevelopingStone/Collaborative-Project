package com.zerobase.oriticket.domain.elasticsearch.transaction.controller;

import com.zerobase.oriticket.domain.elasticsearch.transaction.dto.TransactionSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.service.TransactionSearchService;
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
@RequestMapping("/transactions")
public class TransactionSearchController {

    private final TransactionSearchService transactionSearchService;

    @GetMapping("/search")
    public ResponseEntity<Page<TransactionSearchResponse>> searchByStatus(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<TransactionSearchDocument> transactionSearchDocuments =
                transactionSearchService.searchByStatus(status, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionSearchDocuments.map(TransactionSearchResponse::fromEntity));
    }
}
