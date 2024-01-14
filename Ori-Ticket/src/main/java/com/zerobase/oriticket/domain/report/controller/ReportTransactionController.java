package com.zerobase.oriticket.domain.report.controller;

import com.zerobase.oriticket.domain.report.dto.RegisterReportTransactionRequest;
import com.zerobase.oriticket.domain.report.dto.ReportTransactionResponse;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import com.zerobase.oriticket.domain.report.service.ReportTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class ReportTransactionController {

    private final ReportTransactionService reportTransactionService;

    @PostMapping("/{transactionId}/report")
    public ResponseEntity<ReportTransactionResponse> register(
            @PathVariable("transactionId") Long transactionId,
            @RequestBody RegisterReportTransactionRequest request
    ){
        ReportTransaction reportTransaction = reportTransactionService.register(transactionId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ReportTransactionResponse.fromEntity(reportTransaction));
    }

    @PatchMapping("/report/{reportTransactionId}")
    public ResponseEntity<ReportTransactionResponse> updateToReacted(
            @PathVariable("reportTransactionId") Long reportTransactionId,
            @RequestBody UpdateReportRequest request
            ){
        ReportTransaction reportTransaction = reportTransactionService.updateToReacted(reportTransactionId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ReportTransactionResponse.fromEntity(reportTransaction));
    }
}
