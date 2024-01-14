package com.zerobase.oriticket.domain.elasticsearch.report.controller;

import com.zerobase.oriticket.domain.elasticsearch.report.dto.ReportPostSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.dto.ReportTransactionSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportPostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportTransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.service.ReportPostSearchService;
import com.zerobase.oriticket.domain.elasticsearch.report.service.ReportTransactionSearchService;
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
@RequestMapping("/report")
public class ReportSearchController {

    private final ReportPostSearchService reportPostSearchService;
    private final ReportTransactionSearchService reportTransactionSearchService;

    @GetMapping("/posts/search")
    public ResponseEntity<Page<ReportPostSearchResponse>> searchReportPost(
            @RequestParam(name = "name", required = false) String memberName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<ReportPostSearchDocument> reportPostSearchDocuments =
                reportPostSearchService.search(memberName, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reportPostSearchDocuments.map(reportPostSearchService::entityToDto));
    }

    @GetMapping("/transactions/search")
    public ResponseEntity<Page<ReportTransactionSearchResponse>> searchReportTransaction(
            @RequestParam(name = "name", required = false) String memberName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<ReportTransactionSearchDocument> reportTransactionSearchDocuments =
                reportTransactionSearchService.search(memberName, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reportTransactionSearchDocuments.map(reportTransactionSearchService::entityToDto));
    }
}
