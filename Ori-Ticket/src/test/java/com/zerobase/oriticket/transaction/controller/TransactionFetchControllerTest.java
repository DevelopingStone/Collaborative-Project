package com.zerobase.oriticket.transaction.controller;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.controller.TransactionFetchController;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.service.TransactionFetchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionFetchController.class)
public class TransactionFetchControllerTest {

    @MockBean
    private TransactionFetchService transactionFetchService;

    @Autowired
    private MockMvc mockMvc;

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
    void successGetByMemberIdAndTransactionStatus() throws Exception {
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

        given(transactionFetchService.get(anyLong(), anyList()))
                .willReturn(transactionList);

        //when
        //then
        mockMvc.perform(get("/members/1/transactions?status=pending, received"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].transactionId").value(1L))
                .andExpect(jsonPath("$.[0].salePostId").value(1L))
                .andExpect(jsonPath("$.[0].sellerId").value(1L))
                .andExpect(jsonPath("$.[0].buyerId").value(2L))
                .andExpect(jsonPath("$.[0].payAmount").doesNotExist())
                .andExpect(jsonPath("$.[0].status").value(TransactionStatus.PENDING.getState()))
                .andExpect(jsonPath("$.[0].receivedAt").doesNotExist())
                .andExpect(jsonPath("$.[0].startedAt").exists())
                .andExpect(jsonPath("$.[0].endedAt").doesNotExist())
                .andExpect(jsonPath("$.[1].transactionId").value(2L))
                .andExpect(jsonPath("$.[1].salePostId").value(2L))
                .andExpect(jsonPath("$.[1].sellerId").value(2L))
                .andExpect(jsonPath("$.[1].buyerId").value(1L))
                .andExpect(jsonPath("$.[1].payAmount").value(10000))
                .andExpect(jsonPath("$.[1].status").value(TransactionStatus.RECEIVED.getState()))
                .andExpect(jsonPath("$.[1].receivedAt").exists())
                .andExpect(jsonPath("$.[1].startedAt").exists())
                .andExpect(jsonPath("$.[1].endedAt").doesNotExist());

    }

    @Test
    @DisplayName("특정 멤버의 거래종료 된 Transaction 조회 성공")
    void successGetByMemberIdAndEndStatus() throws Exception {
        //given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Post salePost1 = createPost(1L, member1);
        Post salePost2 = createPost(2L, member2);

        Transaction transaction1 = createTransaction(1L, salePost1, member2,
                10000, TransactionStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());
        Transaction transaction2 = createTransaction(2L, salePost2, member1,
                10000, TransactionStatus.CANCELED, LocalDateTime.now() ,LocalDateTime.now());

        List<Transaction> transactionList = Arrays.asList(transaction1, transaction2);

        given(transactionFetchService.get(anyLong(), anyList()))
                .willReturn(transactionList);

        //when
        //then
        mockMvc.perform(get("/members/1/transactions?status=completed, canceled"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].transactionId").value(1L))
                .andExpect(jsonPath("$.[0].salePostId").value(1L))
                .andExpect(jsonPath("$.[0].sellerId").value(1L))
                .andExpect(jsonPath("$.[0].buyerId").value(2L))
                .andExpect(jsonPath("$.[0].payAmount").value(10000))
                .andExpect(jsonPath("$.[0].status").value(TransactionStatus.COMPLETED.getState()))
                .andExpect(jsonPath("$.[0].receivedAt").exists())
                .andExpect(jsonPath("$.[0].startedAt").exists())
                .andExpect(jsonPath("$.[0].endedAt").exists())
                .andExpect(jsonPath("$.[1].transactionId").value(2L))
                .andExpect(jsonPath("$.[1].salePostId").value(2L))
                .andExpect(jsonPath("$.[1].sellerId").value(2L))
                .andExpect(jsonPath("$.[1].buyerId").value(1L))
                .andExpect(jsonPath("$.[1].payAmount").value(10000))
                .andExpect(jsonPath("$.[1].status").value(TransactionStatus.CANCELED.getState()))
                .andExpect(jsonPath("$.[1].receivedAt").exists())
                .andExpect(jsonPath("$.[1].startedAt").exists())
                .andExpect(jsonPath("$.[1].endedAt").exists());
    }

}
