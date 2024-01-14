package com.zerobase.oriticket.domain.elasticsearch.report.repository;

import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportPostSearchDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReportPostSearchRepository
        extends ElasticsearchRepository<ReportPostSearchDocument, Long> {

    Page<ReportPostSearchDocument> findAllByMemberNameContaining(String memberName, Pageable pageable);
}
