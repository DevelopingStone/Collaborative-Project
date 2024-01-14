package com.zerobase.oriticket.domain.transaction.dto;

import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {

    private Long transactionId;
    private Long salePostId;
    private Long sellerId;
    private Long buyerId;
    private Integer payAmount;
    private String status;
    private LocalDateTime receivedAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static TransactionResponse fromEntity(Transaction transaction){
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .salePostId(transaction.getSalePost().getSalePostId())
                .sellerId(transaction.getSalePost().getMember().getMemberId())
                .buyerId(transaction.getMember().getMemberId())
                .payAmount(transaction.getPayAmount())
                .status(transaction.getStatus().getState())
                .receivedAt(transaction.getReceivedAt())
                .startedAt(transaction.getStartedAt())
                .endedAt(transaction.getEndedAt())
                .build();
    }

}
