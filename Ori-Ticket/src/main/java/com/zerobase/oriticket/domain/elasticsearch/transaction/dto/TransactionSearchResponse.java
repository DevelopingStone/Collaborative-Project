package com.zerobase.oriticket.domain.elasticsearch.transaction.dto;

import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TransactionSearchResponse {

    private Long transactionId;
    private Long salePostId;
    private String sellerName;
    private String buyerName;
    private Integer payAmount;
    private String status;
    private LocalDateTime receivedAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static TransactionSearchResponse fromEntity(TransactionSearchDocument transaction){
        return TransactionSearchResponse.builder()
                .transactionId(transaction.getTransactionId())
                .salePostId(transaction.getSalePostId())
                .sellerName(transaction.getSellerName())
                .buyerName(transaction.getBuyerName())
                .payAmount(transaction.getPayAmount())
                .status(transaction.getStatus().getState())
                .receivedAt(transaction.getReceivedAt())
                .startedAt(transaction.getStartedAt())
                .endedAt(transaction.getEndedAt())
                .build();
    }
}
