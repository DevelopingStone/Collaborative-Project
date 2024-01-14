package com.zerobase.oriticket.domain.elasticsearch.report.service;

import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.elasticsearch.report.dto.ReportPostSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportPostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.repository.ReportPostSearchRepository;
import com.zerobase.oriticket.global.constants.PostExceptionStatus;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportPostSearchService {

    private final ReportPostSearchRepository reportPostSearchRepository;
    private final PostSearchRepository postSearchRepository;

    private final static String REPORTED_AT = "reportedAt";
    private final Sort sort = Sort.by(REPORTED_AT).descending();

    public Page<ReportPostSearchDocument> search(String memberName, int page, int size) {

        if(memberName == null){
            return searchAll(page, size);
        }

        return searchByMemberName(memberName, page, size);
    }

    public ReportPostSearchResponse entityToDto(ReportPostSearchDocument reportPostSearchDocument){

        PostSearchDocument postSearchDocument =
                postSearchRepository.findById(reportPostSearchDocument.getSalePostId())
                .orElseThrow(() -> new CustomException(PostExceptionStatus.SALE_POST_NOT_FOUND.getCode(),
                        PostExceptionStatus.SALE_POST_NOT_FOUND.getMessage()));

        return ReportPostSearchResponse.fromEntity(reportPostSearchDocument, postSearchDocument);
    }

    private Page<ReportPostSearchDocument> searchAll(int page, int size){

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return reportPostSearchRepository.findAll(pageable);
    }

    private Page<ReportPostSearchDocument> searchByMemberName(String memberName, int page, int size) {

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return reportPostSearchRepository.findAllByMemberNameContaining(memberName, pageable);
    }
}
