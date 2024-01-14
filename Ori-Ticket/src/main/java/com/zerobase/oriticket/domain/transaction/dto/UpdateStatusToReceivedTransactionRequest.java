package com.zerobase.oriticket.domain.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusToReceivedTransactionRequest {
    private Long transactionId;
    private Integer payAmount;
}
