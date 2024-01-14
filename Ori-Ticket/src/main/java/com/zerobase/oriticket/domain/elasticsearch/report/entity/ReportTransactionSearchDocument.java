package com.zerobase.oriticket.domain.elasticsearch.report.entity;

import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "report-transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReportTransactionSearchDocument {

    @Id
    private Long reportTransactionId;

    private String memberName;
    private Long transactionId;
    private String reason;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime reportedAt;

    private ReportReactStatus status;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime reactedAt;
    private String note;

    public static ReportTransactionSearchDocument fromEntity(ReportTransaction reportTransaction){
        return ReportTransactionSearchDocument.builder()
                .reportTransactionId(reportTransaction.getReportTransactionId())
                .memberName(reportTransaction.getMember().getNickname())
                .transactionId(reportTransaction.getTransaction().getTransactionId())
                .reason(reportTransaction.getReason().getReportType())
                .reportedAt(reportTransaction.getReportedAt())
                .status(reportTransaction.getStatus())
                .reactedAt(reportTransaction.getReactedAt())
                .note(reportTransaction.getNote())
                .build();
    }
}
