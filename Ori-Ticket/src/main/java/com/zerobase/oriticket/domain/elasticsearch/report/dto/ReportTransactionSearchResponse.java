package com.zerobase.oriticket.domain.elasticsearch.report.dto;

import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportTransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.dto.TransactionSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReportTransactionSearchResponse {

    private Long reportTransactionId;
    private String memberName;
    private TransactionSearchResponse transaction;
    private String reason;
    private LocalDateTime reportedAt;
    private ReportReactStatus status;
    private LocalDateTime reactedAt;
    private String note;

    public static ReportTransactionSearchResponse fromEntity(
            ReportTransactionSearchDocument reportPostSearchDocument,
            TransactionSearchDocument transactionSearchDocument
    ){
        return ReportTransactionSearchResponse.builder()
                .reportTransactionId(reportPostSearchDocument.getReportTransactionId())
                .memberName(reportPostSearchDocument.getMemberName())
                .transaction(TransactionSearchResponse.fromEntity(transactionSearchDocument))
                .reason(reportPostSearchDocument.getReason())
                .reportedAt(reportPostSearchDocument.getReportedAt())
                .status(reportPostSearchDocument.getStatus())
                .reactedAt(reportPostSearchDocument.getReactedAt())
                .note(reportPostSearchDocument.getNote())
                .build();
    }
}
