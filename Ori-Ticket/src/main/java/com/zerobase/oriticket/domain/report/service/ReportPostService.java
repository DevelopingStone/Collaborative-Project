package com.zerobase.oriticket.domain.report.service;

import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportPostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.repository.ReportPostSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.dto.RegisterReportPostRequest;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import com.zerobase.oriticket.domain.report.repository.ReportPostRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.zerobase.oriticket.global.constants.MemberExceptionStatus.MEMBER_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.PostExceptionStatus.SALE_POST_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.ReportExceptionStatus.CANNOT_REGISTER_REPORT_POST;
import static com.zerobase.oriticket.global.constants.ReportExceptionStatus.REPORT_POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReportPostService {

    private final ReportPostRepository reportPostRepository;
    private final ReportPostSearchRepository reportPostSearchRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReportPost register(Long salePostId, RegisterReportPostRequest request) {

        Member member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND.getCode(), MEMBER_NOT_FOUND.getMessage()));

        Post salePost = postRepository.findById(salePostId)
                .orElseThrow(() -> new CustomException(SALE_POST_NOT_FOUND.getCode(),
                        SALE_POST_NOT_FOUND.getMessage()));

        validateCanRegister(salePostId, request);

        ReportPost reportPost = reportPostRepository.save(request.toEntity(member, salePost));
        reportPostSearchRepository.save(ReportPostSearchDocument.fromEntity(reportPost));

        return reportPost;
    }

    @Transactional
    public ReportPost updateToReacted(Long reportPostId, UpdateReportRequest request) {
        ReportPost reportPost = reportPostRepository.findById(reportPostId)
                .orElseThrow(() -> new CustomException(REPORT_POST_NOT_FOUND.getCode(),
                        REPORT_POST_NOT_FOUND.getMessage()));

        reportPost.setStatus(ReportReactStatus.REACTED);
        reportPost.setReactedAt(LocalDateTime.now());
        reportPost.setNote(request.getNote());
        reportPostSearchRepository.save(ReportPostSearchDocument.fromEntity(reportPost));

        return reportPostRepository.save(reportPost);
    }

    private void validateCanRegister(Long salePostId, RegisterReportPostRequest request){
        if(reportPostRepository.existsBySalePost_SalePostIdAndMember_MemberIdAndReason(
                salePostId, request.getMemberId(), ReportPostType.valueOf(request.getReason().toUpperCase()))
        ){
            throw new CustomException(CANNOT_REGISTER_REPORT_POST.getCode(),
                    CANNOT_REGISTER_REPORT_POST.getMessage());
        }
    }
}
