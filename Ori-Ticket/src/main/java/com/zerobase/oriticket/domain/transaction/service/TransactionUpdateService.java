package com.zerobase.oriticket.domain.transaction.service;

import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ChatRoomRepository;
import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.repository.TransactionSearchRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusToReceivedTransactionRequest;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusTransactionRequest;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.zerobase.oriticket.global.constants.ChatExceptionStatus.CHAT_ROOM_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.TransactionExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class TransactionUpdateService {

    private final TransactionRepository transactionRepository;
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final TransactionSearchRepository transactionSearchRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Transaction updateToReceived(UpdateStatusToReceivedTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new CustomException(TRANSACTION_NOT_FOUND.getCode(), TRANSACTION_NOT_FOUND.getMessage()));

        validateCanUpdateStatus(transaction.getStatus());

        transaction.setPayAmount(request.getPayAmount());
        transaction.setStatus(TransactionStatus.RECEIVED);
        transaction.setReceivedAt(LocalDateTime.now());
        transactionSearchRepository.save(TransactionSearchDocument.fromEntity(transaction));

        chatRoomRepository.save(ChatRoom.createChatRoom(transaction, transaction.getSalePost()));

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateToCompleted(UpdateStatusTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new CustomException(TRANSACTION_NOT_FOUND.getCode(),
                        TRANSACTION_NOT_FOUND.getMessage()));

        validateCanUpdateStatus(transaction.getStatus());

        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setEndedAt(LocalDateTime.now());
        transactionSearchRepository.save(TransactionSearchDocument.fromEntity(transaction));

        Post salePost = transaction.getSalePost();
        salePost.setSaleStatus(SaleStatus.SOLD);
        postRepository.save(salePost);
        postSearchRepository.save(PostSearchDocument.fromEntity(salePost));

        endChatRoom(transaction.getTransactionId());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateToCanceled(UpdateStatusTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new CustomException(TRANSACTION_NOT_FOUND.getCode(), TRANSACTION_NOT_FOUND.getMessage()));

        validateCanUpdateStatus(transaction.getStatus());

        transaction.setStatus(TransactionStatus.CANCELED);
        transaction.setEndedAt(LocalDateTime.now());
        transactionSearchRepository.save(TransactionSearchDocument.fromEntity(transaction));

        Post salePost = transaction.getSalePost();
        salePost.setSaleStatus(SaleStatus.FOR_SALE);
        postRepository.save(salePost);
        postSearchRepository.save(PostSearchDocument.fromEntity(salePost));

        endChatRoom(transaction.getTransactionId());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateToReported(UpdateStatusTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new CustomException(TRANSACTION_NOT_FOUND.getCode(), TRANSACTION_NOT_FOUND.getMessage()));

        validateCanUpdateStatus(transaction.getStatus());

        transaction.setStatus(TransactionStatus.REPORTED);
        transaction.setEndedAt(LocalDateTime.now());
        transactionSearchRepository.save(TransactionSearchDocument.fromEntity(transaction));

        Post salePost = transaction.getSalePost();
        salePost.setSaleStatus(SaleStatus.REPORTED);
        postRepository.save(salePost);
        postSearchRepository.save(PostSearchDocument.fromEntity(salePost));

        endChatRoom(transaction.getTransactionId());

        return transactionRepository.save(transaction);
    }

    private void validateCanUpdateStatus(TransactionStatus status){
        if(status == TransactionStatus.CANCELED){
            throw new CustomException(CANNOT_MODIFY_TRANSACTION_STATE_OF_CANCELED.getCode(),
                    CANNOT_MODIFY_TRANSACTION_STATE_OF_CANCELED.getMessage());
        }
        if(status == TransactionStatus.COMPLETED){
            throw new CustomException(CANNOT_MODIFY_TRANSACTION_STATE_OF_COMPLETED.getCode(),
                    CANNOT_MODIFY_TRANSACTION_STATE_OF_COMPLETED.getMessage());
        }
        if(status == TransactionStatus.REPORTED){
            throw new CustomException(CANNOT_MODIFY_TRANSACTION_STATE_OF_REPORTED.getCode(),
                    CANNOT_MODIFY_TRANSACTION_STATE_OF_REPORTED.getMessage());
        }
    }

    private void endChatRoom(Long transactionId){
        if(chatRoomRepository.existsByTransaction_TransactionId(transactionId)){
            ChatRoom chatRoom = chatRoomRepository.findByTransaction_TransactionId(transactionId)
                    .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND.getCode(), CHAT_ROOM_NOT_FOUND.getMessage()));

            chatRoom.setEndedAt(LocalDateTime.now());

            chatRoomRepository.save(chatRoom);
        }
    }
}
