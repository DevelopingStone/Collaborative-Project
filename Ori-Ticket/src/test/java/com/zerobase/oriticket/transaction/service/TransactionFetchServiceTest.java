package com.zerobase.oriticket.transaction.service;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.domain.transaction.service.TransactionFetchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionFetchServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionFetchService transactionFetchService;

    private Post createPost(Long salePostId, Member member){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    private Transaction createTransaction(
            Long transactionId,
            Post salePost,
            Member buyer,
            Integer payAmount,
            TransactionStatus status,
            LocalDateTime receivedAt,
            LocalDateTime endedAt
    ){
        return Transaction.builder()
                .transactionId(transactionId)
                .salePost(salePost)
                .member(buyer)
                .payAmount(payAmount)
                .status(status)
                .receivedAt(receivedAt)
                .startedAt(LocalDateTime.now())
                .endedAt(endedAt)
                .build();
    }

    @Test
    @DisplayName("특정 멤버의 거래중인 Transaction 조회 성공")
    void successGetTransaction(){
        //given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Post salePost1 = createPost(1L, member1);
        Post salePost2 = createPost(2L, member2);

        Transaction transaction1 = createTransaction(1L, salePost1, member2,
                null, TransactionStatus.PENDING, null, null);
        Transaction transaction2 = createTransaction(2L, salePost2, member1,
                10000, TransactionStatus.RECEIVED, LocalDateTime.now(), null);

        List<Transaction> transactionList = Arrays.asList(transaction1, transaction2);
        List<TransactionStatus> transactionStatusList =
                Arrays.asList(TransactionStatus.PENDING, TransactionStatus.RECEIVED);

        given(transactionRepository.findAllBySellerOrBuyerIdAndStatusList(anyLong(), anyList()))
                .willReturn(transactionList);

        //when
        List<Transaction> fetchedTransactions = transactionFetchService.get(1L, transactionStatusList);

        //then
        assertThat(fetchedTransactions.get(0).getTransactionId()).isEqualTo(1L);
        assertThat(fetchedTransactions.get(0).getSalePost()).isEqualTo(salePost1);
        assertThat(fetchedTransactions.get(0).getMember().getMemberId()).isEqualTo(2L);
        assertNull(fetchedTransactions.get(0).getPayAmount());
        assertThat(fetchedTransactions.get(0).getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertNull(fetchedTransactions.get(0).getReceivedAt());
        assertNotNull(fetchedTransactions.get(0).getStartedAt());
        assertNull(fetchedTransactions.get(0).getEndedAt());
        assertThat(fetchedTransactions.get(1).getTransactionId()).isEqualTo(2L);
        assertThat(fetchedTransactions.get(1).getSalePost()).isEqualTo(salePost2);
        assertThat(fetchedTransactions.get(1).getMember().getMemberId()).isEqualTo(1L);
        assertThat(fetchedTransactions.get(1).getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactions.get(1).getStatus()).isEqualTo(TransactionStatus.RECEIVED);
        assertNotNull(fetchedTransactions.get(1).getReceivedAt());
        assertNotNull(fetchedTransactions.get(1).getStartedAt());
        assertNull(fetchedTransactions.get(1).getEndedAt());

    }

    @Test
    @DisplayName("특정 멤버의 거래완료 된 Transaction 조회 성공")
    void successGetEnd(){
        //given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Post salePost1 = createPost(1L, member1);
        Post salePost2 = createPost(2L, member2);

        Transaction transaction1 = createTransaction(1L, salePost1, member2,
                10000, TransactionStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        Transaction transaction2 = createTransaction(2L, salePost2, member1,
                10000, TransactionStatus.CANCELED, LocalDateTime.now(), LocalDateTime.now());

        List<Transaction> transactionList = Arrays.asList(transaction1, transaction2);
        List<TransactionStatus> transactionStatusList =
                Arrays.asList(TransactionStatus.COMPLETED, TransactionStatus.CANCELED);

        given(transactionRepository.findAllBySellerOrBuyerIdAndStatusList(anyLong(), anyList()))
                .willReturn(transactionList);

        //when
        List<Transaction> fetchedTransactions = transactionFetchService.get(1L, transactionStatusList);

        //then
        assertThat(fetchedTransactions.get(0).getTransactionId()).isEqualTo(1L);
        assertThat(fetchedTransactions.get(0).getSalePost()).isEqualTo(salePost1);
        assertThat(fetchedTransactions.get(0).getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedTransactions.get(1).getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactions.get(0).getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertNotNull(fetchedTransactions.get(0).getReceivedAt());
        assertNotNull(fetchedTransactions.get(0).getStartedAt());
        assertNotNull(fetchedTransactions.get(0).getEndedAt());
        assertThat(fetchedTransactions.get(1).getTransactionId()).isEqualTo(2L);
        assertThat(fetchedTransactions.get(1).getSalePost()).isEqualTo(salePost2);
        assertThat(fetchedTransactions.get(1).getMember().getMemberId()).isEqualTo(1L);
        assertThat(fetchedTransactions.get(1).getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactions.get(1).getStatus()).isEqualTo(TransactionStatus.CANCELED);
        assertNotNull(fetchedTransactions.get(1).getReceivedAt());
        assertNotNull(fetchedTransactions.get(1).getStartedAt());
        assertNotNull(fetchedTransactions.get(1).getEndedAt());
    }


}
