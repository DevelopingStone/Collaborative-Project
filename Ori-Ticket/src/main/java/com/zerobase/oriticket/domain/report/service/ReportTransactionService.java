package com.zerobase.oriticket.domain.report.service;

import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportTransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.repository.ReportTransactionSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.constants.ReportTransactionType;
import com.zerobase.oriticket.domain.report.dto.RegisterReportTransactionRequest;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import com.zerobase.oriticket.domain.report.repository.ReportTransactionRepository;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.zerobase.oriticket.global.constants.MemberExceptionStatus.MEMBER_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.ReportExceptionStatus.CANNOT_REGISTER_REPORT_TRANSACTION;
import static com.zerobase.oriticket.global.constants.ReportExceptionStatus.REPORT_TRANSACTION_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.TransactionExceptionStatus.TRANSACTION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReportTransactionService {

    private final ReportTransactionRepository reportTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final ReportTransactionSearchRepository reportTransactionSearchRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReportTransaction register(Long transactionId, RegisterReportTransactionRequest request) {

        Member member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND.getCode(), MEMBER_NOT_FOUND.getMessage()));

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(TRANSACTION_NOT_FOUND.getCode(),
                        TRANSACTION_NOT_FOUND.getMessage()));

        validateCanRegister(transactionId, request);

        ReportTransaction reportTransaction = reportTransactionRepository.save(request.toEntity(member, transaction));
        reportTransactionSearchRepository.save(ReportTransactionSearchDocument.fromEntity(reportTransaction));

        return reportTransaction;
    }

    @Transactional
    public ReportTransaction updateToReacted(Long reportTransactionId, UpdateReportRequest request) {
        ReportTransaction reportTransaction = reportTransactionRepository.findById(reportTransactionId)
                .orElseThrow(() -> new CustomException(REPORT_TRANSACTION_NOT_FOUND.getCode(),
                        REPORT_TRANSACTION_NOT_FOUND.getMessage()));

        reportTransaction.setStatus(ReportReactStatus.REACTED);
        reportTransaction.setReactedAt(LocalDateTime.now());
        reportTransaction.setNote(request.getNote());
        reportTransactionSearchRepository.save(ReportTransactionSearchDocument.fromEntity(reportTransaction));

        return reportTransactionRepository.save(reportTransaction);
    }

    private void validateCanRegister(Long transactionId, RegisterReportTransactionRequest request){
        if(reportTransactionRepository.existsByTransaction_TransactionIdAndMember_MemberIdAndReason(
                transactionId, request.getMemberId(), ReportTransactionType.valueOf(request.getReason().toUpperCase()))
        ){
            throw new CustomException(CANNOT_REGISTER_REPORT_TRANSACTION.getCode(),
                    CANNOT_REGISTER_REPORT_TRANSACTION.getMessage());
        }
    }
}
