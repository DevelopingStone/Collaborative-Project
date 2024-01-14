package com.zerobase.oriticket.domain.elasticsearch.report.service;

import com.zerobase.oriticket.domain.elasticsearch.report.dto.ReportTransactionSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportTransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.repository.ReportTransactionSearchRepository;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.repository.TransactionSearchRepository;
import com.zerobase.oriticket.global.constants.TransactionExceptionStatus;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportTransactionSearchService {

    private final ReportTransactionSearchRepository reportTransactionSearchRepository;
    private final TransactionSearchRepository transactionSearchRepository;

    private final static String REPORTED_AT = "reportedAt";
    private final Sort sort = Sort.by(REPORTED_AT).descending();

    public Page<ReportTransactionSearchDocument> search(String memberName, int page, int size) {

        if(memberName == null){
            return searchAll(page, size);
        }

        return searchByMemberName(memberName, page, size);
    }

    public ReportTransactionSearchResponse entityToDto(ReportTransactionSearchDocument reportTransactionSearchDocument){

        TransactionSearchDocument transactionSearchDocument =
                transactionSearchRepository.findById(reportTransactionSearchDocument.getTransactionId())
                .orElseThrow(() -> new CustomException(TransactionExceptionStatus.TRANSACTION_NOT_FOUND.getCode(),
                        TransactionExceptionStatus.TRANSACTION_NOT_FOUND.getMessage()));

        return ReportTransactionSearchResponse.fromEntity(reportTransactionSearchDocument, transactionSearchDocument);
    }

    private Page<ReportTransactionSearchDocument> searchAll(int page, int size){

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return reportTransactionSearchRepository.findAll(pageable);
    }

    private Page<ReportTransactionSearchDocument> searchByMemberName(String memberName, int page, int size) {

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return reportTransactionSearchRepository.findAllByMemberNameContaining(memberName, pageable);
    }
}
