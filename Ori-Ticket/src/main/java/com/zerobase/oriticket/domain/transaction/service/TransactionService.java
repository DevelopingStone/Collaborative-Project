package com.zerobase.oriticket.domain.transaction.service;

import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.repository.TransactionSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.transaction.dto.RegisterTransactionRequest;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.oriticket.global.constants.MemberExceptionStatus.MEMBER_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.PostExceptionStatus.SALE_POST_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.TransactionExceptionStatus.TRANSACTION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionSearchRepository transactionSearchRepository;
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final UserRepository userRepository;

    private static final String STARTED_AT = "startedAt";

    @Transactional
    public Transaction register(RegisterTransactionRequest request){

        Post salePost = postRepository.findById(request.getSalePostId())
                .orElseThrow(() -> new CustomException(SALE_POST_NOT_FOUND.getCode(), SALE_POST_NOT_FOUND.getMessage()));

        Member member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND.getCode(), MEMBER_NOT_FOUND.getMessage()));

        if (!transactionRepository.validateCanRegisterTransaction(salePost)){
            throw new CustomException(TRANSACTION_NOT_FOUND.getCode(), TRANSACTION_NOT_FOUND.getMessage());
        }

        Transaction transaction = transactionRepository.save(request.toEntity(salePost, member));
        transactionSearchRepository.save(TransactionSearchDocument.fromEntity(transaction));
        salePost.setSaleStatus(SaleStatus.TRADING);
        postRepository.save(salePost);
        postSearchRepository.save(PostSearchDocument.fromEntity(salePost));

        return transaction;
    }

    @Transactional(readOnly = true)
    public Transaction get(Long transactionId){

        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(TRANSACTION_NOT_FOUND.getCode(), TRANSACTION_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getAll(int page, int size) {
        Sort sort = Sort.by(STARTED_AT).descending();

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return transactionRepository.findAll(pageable);
    }
}
