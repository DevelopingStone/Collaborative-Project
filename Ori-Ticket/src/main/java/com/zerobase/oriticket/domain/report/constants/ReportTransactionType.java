package com.zerobase.oriticket.domain.report.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportTransactionType {
    ECONOMIC_LOSS("Economic loss"),
    MATERIAL_LOSS("Material loss");

    private String reportType;
}
