package com.zerobase.oriticket.domain.report.dto;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReportPostRequest {

    private Long memberId;
    private String reason;

    public ReportPost toEntity(Member member, Post salePost){

        ReportPostType reportType = ReportPostType.valueOf(reason.toUpperCase());

        return ReportPost.builder()
                .member(member)
                .salePost(salePost)
                .reason(reportType)
                .status(ReportReactStatus.PROCESSING)
                .build();
    }
}
