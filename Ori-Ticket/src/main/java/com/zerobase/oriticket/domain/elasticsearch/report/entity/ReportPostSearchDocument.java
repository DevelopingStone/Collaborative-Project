package com.zerobase.oriticket.domain.elasticsearch.report.entity;

import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
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

@Document(indexName = "report-post")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReportPostSearchDocument {

    @Id
    private Long reportPostId;

    private String memberName;
    private Long salePostId;
    private String reason;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime reportedAt;

    private ReportReactStatus status;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime reactedAt;
    private String note;

    public static ReportPostSearchDocument fromEntity(ReportPost reportPost) {
        return ReportPostSearchDocument.builder()
                .reportPostId(reportPost.getReportPostId())
                .memberName(reportPost.getMember().getNickname())
                .salePostId(reportPost.getSalePost().getSalePostId())
                .reason(reportPost.getReason().getReportType())
                .reportedAt(reportPost.getReportedAt())
                .status(reportPost.getStatus())
                .reactedAt(reportPost.getReactedAt())
                .note(reportPost.getNote())
                .build();
    }
}
