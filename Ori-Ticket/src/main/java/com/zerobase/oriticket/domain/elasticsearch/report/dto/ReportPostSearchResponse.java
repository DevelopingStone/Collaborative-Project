package com.zerobase.oriticket.domain.elasticsearch.report.dto;

import com.zerobase.oriticket.domain.elasticsearch.post.dto.PostSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportPostSearchDocument;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReportPostSearchResponse {

    private Long reportPostId;
    private String memberName;
    private PostSearchResponse salePost;
    private String reason;
    private LocalDateTime reportedAt;
    private ReportReactStatus status;
    private LocalDateTime reactedAt;
    private String note;

    public static ReportPostSearchResponse fromEntity(
            ReportPostSearchDocument reportPostSearchDocument,
            PostSearchDocument postSearchDocument
    ){
        return ReportPostSearchResponse.builder()
                .reportPostId(reportPostSearchDocument.getReportPostId())
                .memberName(reportPostSearchDocument.getMemberName())
                .salePost(PostSearchResponse.fromEntity(postSearchDocument))
                .reason(reportPostSearchDocument.getReason())
                .reportedAt(reportPostSearchDocument.getReportedAt())
                .status(reportPostSearchDocument.getStatus())
                .reactedAt(reportPostSearchDocument.getReactedAt())
                .note(reportPostSearchDocument.getNote())
                .build();
    }
}
