package com.zerobase.oriticket.domain.elasticsearch.transaction.service;

import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.repository.TransactionSearchRepository;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionSearchService {

    private final TransactionSearchRepository transactionSearchRepository;

    private final static String STARTED_AT = "startedAt";
    private final Sort sort = Sort.by(STARTED_AT).descending();

    public Page<TransactionSearchDocument> searchByStatus(String status, int page, int size){

        TransactionStatus transactionStatus = TransactionStatus.valueOf(status.toUpperCase());

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return transactionSearchRepository.findAllByStatus(transactionStatus, pageable);
    }

}
