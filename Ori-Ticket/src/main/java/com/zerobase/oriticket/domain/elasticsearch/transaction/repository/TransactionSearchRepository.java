package com.zerobase.oriticket.domain.elasticsearch.transaction.repository;

import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TransactionSearchRepository extends ElasticsearchRepository<TransactionSearchDocument, Long> {
    Page<TransactionSearchDocument> findAllByStatus(TransactionStatus transactionStatus, Pageable pageable);
}
