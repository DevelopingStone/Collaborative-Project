package com.zerobase.oriticket.domain.report.dto;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.constants.ReportTransactionType;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReportTransactionRequest {

    private Long memberId;
    private String reason;

    public ReportTransaction toEntity(Member member, Transaction transaction){

        ReportTransactionType reportType = ReportTransactionType.valueOf(reason.toUpperCase());

        return ReportTransaction.builder()
                .member(member)
                .transaction(transaction)
                .reason(reportType)
                .status(ReportReactStatus.PROCESSING)
                .build();
    }
}
