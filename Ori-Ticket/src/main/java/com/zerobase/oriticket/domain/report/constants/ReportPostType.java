package com.zerobase.oriticket.domain.report.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportPostType {

    OVERPRICED_REGISTRATION("Overpriced registration"),
    SUSPECTED_FALSE_INFORMATION("Suspected false information"),
    INAPPROPRIATE_PHOTO("Inappropriate photo"),
    OTHER_ISSUES("Other issues");

    private String reportType;
}
