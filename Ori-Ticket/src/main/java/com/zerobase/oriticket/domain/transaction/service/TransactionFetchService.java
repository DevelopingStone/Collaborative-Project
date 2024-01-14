package com.zerobase.oriticket.domain.transaction.service;

import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionFetchService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<Transaction> get(Long memberId, List<TransactionStatus> transactionStatusList) {
        return transactionRepository.findAllBySellerOrBuyerIdAndStatusList(memberId, transactionStatusList);
    }
}
