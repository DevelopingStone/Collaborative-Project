package com.zerobase.oriticket.domain.report.dto;

import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import com.zerobase.oriticket.domain.transaction.dto.TransactionResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportTransactionResponse {

    private Long reportTransactionId;
    private Long memberId;
    private TransactionResponse transaction;
    private String reason;
    private LocalDateTime reportedAt;
    private ReportReactStatus status;
    private LocalDateTime reactedAt;
    private String note;

    public static ReportTransactionResponse fromEntity(ReportTransaction reportTransaction){

        return ReportTransactionResponse.builder()
                .reportTransactionId(reportTransaction.getReportTransactionId())
                .memberId(reportTransaction.getMember().getMemberId())
                .transaction(TransactionResponse.fromEntity(reportTransaction.getTransaction()))
                .reason(reportTransaction.getReason().getReportType())
                .reportedAt(reportTransaction.getReportedAt())
                .status(reportTransaction.getStatus())
                .reactedAt(reportTransaction.getReactedAt())
                .note(reportTransaction.getNote())
                .build();
    }
}
