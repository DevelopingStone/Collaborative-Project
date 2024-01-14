package com.zerobase.oriticket.domain.transaction.controller;

import com.zerobase.oriticket.domain.transaction.dto.RegisterTransactionRequest;
import com.zerobase.oriticket.domain.transaction.dto.TransactionResponse;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusToReceivedTransactionRequest;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusTransactionRequest;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.service.TransactionService;
import com.zerobase.oriticket.domain.transaction.service.TransactionUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionUpdateService transactionUpdateService;

    @PostMapping
    public ResponseEntity<TransactionResponse> register(
            @RequestBody RegisterTransactionRequest registerRequest
    ){

        Transaction transaction = transactionService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(TransactionResponse.fromEntity(transaction));
    }

    @GetMapping
    public ResponseEntity<TransactionResponse> get(
            @RequestParam("id") Long transactionId
    ){

        Transaction transaction = transactionService.get(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(TransactionResponse.fromEntity(transaction));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<TransactionResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){

        Page<Transaction> transactions = transactionService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(transactions.map(TransactionResponse::fromEntity));
    }

    @PatchMapping("/receive")
    public ResponseEntity<TransactionResponse> updateToReceived(
            @RequestBody UpdateStatusToReceivedTransactionRequest request
    ){
        Transaction transaction = transactionUpdateService.updateToReceived(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(TransactionResponse.fromEntity(transaction));
    }

    @PatchMapping("/completion")
    public ResponseEntity<TransactionResponse> updateToCompleted(
            @RequestBody UpdateStatusTransactionRequest request
    ){
        Transaction transaction = transactionUpdateService.updateToCompleted(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(TransactionResponse.fromEntity(transaction));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<TransactionResponse> updateToCanceled(
            @RequestBody UpdateStatusTransactionRequest request
    ){
        Transaction transaction = transactionUpdateService.updateToCanceled(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(TransactionResponse.fromEntity(transaction));
    }

    @PatchMapping("/report")
    public ResponseEntity<TransactionResponse> updateToReported(
            @RequestBody UpdateStatusTransactionRequest request
    ){
        Transaction transaction = transactionUpdateService.updateToReported(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(TransactionResponse.fromEntity(transaction));
    }
}
