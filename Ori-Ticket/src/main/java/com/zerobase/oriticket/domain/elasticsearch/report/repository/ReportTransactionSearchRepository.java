package com.zerobase.oriticket.domain.elasticsearch.report.repository;

import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportTransactionSearchDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReportTransactionSearchRepository
        extends ElasticsearchRepository<ReportTransactionSearchDocument, Long> {
    Page<ReportTransactionSearchDocument> findAllByMemberNameContaining(String memberName, Pageable pageable);
}
