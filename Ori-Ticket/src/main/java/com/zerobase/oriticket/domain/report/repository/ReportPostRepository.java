package com.zerobase.oriticket.domain.report.repository;

import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
    boolean existsBySalePost_SalePostIdAndMember_MemberIdAndReason
            (Long salePostId, Long memberId, ReportPostType reason);
}
