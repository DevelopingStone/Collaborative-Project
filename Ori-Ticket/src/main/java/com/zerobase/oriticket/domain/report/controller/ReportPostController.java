package com.zerobase.oriticket.domain.report.controller;

import com.zerobase.oriticket.domain.report.dto.RegisterReportPostRequest;
import com.zerobase.oriticket.domain.report.dto.ReportPostResponse;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import com.zerobase.oriticket.domain.report.service.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class ReportPostController {

    private final ReportPostService reportPostService;

    @PostMapping("/{salePostId}/report")
    public ResponseEntity<ReportPostResponse> register(
            @PathVariable("salePostId") Long salePostId,
            @RequestBody RegisterReportPostRequest request
    ){
        ReportPost reportPost = reportPostService.register(salePostId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ReportPostResponse.fromEntity(reportPost));
    }

    @PatchMapping("/report/{reportPostId}")
    public ResponseEntity<ReportPostResponse> updateToReacted(
            @PathVariable("reportPostId") Long reportPostId,
            @RequestBody UpdateReportRequest request
            ){
        ReportPost reportPost = reportPostService.updateToReacted(reportPostId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ReportPostResponse.fromEntity(reportPost));
    }
}
