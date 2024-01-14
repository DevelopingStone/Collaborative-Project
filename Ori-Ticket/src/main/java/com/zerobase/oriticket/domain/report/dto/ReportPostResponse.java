package com.zerobase.oriticket.domain.report.dto;

import com.zerobase.oriticket.domain.post.dto.PostResponse;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportPostResponse {

    private Long reportPostId;
    private Long memberId;
    private PostResponse salePost;
    private String reason;
    private LocalDateTime reportedAt;
    private ReportReactStatus status;
    private LocalDateTime reactedAt;
    private String note;

    public static ReportPostResponse fromEntity(ReportPost reportPost){

        return ReportPostResponse.builder()
                .reportPostId(reportPost.getReportPostId())
                .memberId(reportPost.getMember().getMemberId())
                .salePost(PostResponse.fromEntity(reportPost.getSalePost()))
                .reason(reportPost.getReason().getReportType())
                .reportedAt(reportPost.getReportedAt())
                .status(reportPost.getStatus())
                .reactedAt(reportPost.getReactedAt())
                .note(reportPost.getNote())
                .build();
    }
}
