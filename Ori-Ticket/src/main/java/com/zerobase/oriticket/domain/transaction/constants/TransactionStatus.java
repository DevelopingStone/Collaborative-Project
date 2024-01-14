package com.zerobase.oriticket.domain.transaction.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatus {

    PENDING("Pending"),
    RECEIVED("Received"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    REPORTED("Reported");

    private final String state;
}
