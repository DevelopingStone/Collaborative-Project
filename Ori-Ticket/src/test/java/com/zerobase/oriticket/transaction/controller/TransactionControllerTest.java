package com.zerobase.oriticket.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.controller.TransactionController;
import com.zerobase.oriticket.domain.transaction.dto.RegisterTransactionRequest;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusToReceivedTransactionRequest;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusTransactionRequest;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.service.TransactionService;
import com.zerobase.oriticket.domain.transaction.service.TransactionUpdateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionUpdateService transactionUpdateService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/transactions";
    private final static Long TRANSACTION_ID = 1L;
    private final static Long POST_ID = 1L;
    private final static Long SELLER_ID = 1L;
    private final static Long BUYER_ID = 2L;
    private final static Integer PAY_AMOUNT = 10000;

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
            Member member,
            Integer payAmount,
            TransactionStatus status,
            LocalDateTime receivedAt,
            LocalDateTime endedAt
    ){
        return Transaction.builder()
                .transactionId(transactionId)
                .salePost(salePost)
                .member(member)
                .payAmount(payAmount)
                .status(status)
                .receivedAt(receivedAt)
                .startedAt(LocalDateTime.now())
                .endedAt(endedAt)
                .build();
    }


    @Test
    @DisplayName("Transaction 등록 성공")
    void successRegister() throws Exception {
        //given
        RegisterTransactionRequest transactionRequest =
                RegisterTransactionRequest.builder()
                        .salePostId(POST_ID)
                        .memberId(BUYER_ID)
                        .build();

        Member member1 = createMember(SELLER_ID);
        Member member2 = createMember(BUYER_ID);
        Post salePost = createPost(POST_ID, member1);
        Transaction transaction =
                createTransaction(TRANSACTION_ID, salePost, member2, null,
                        TransactionStatus.PENDING, null, null);

        given(transactionService.register(any(RegisterTransactionRequest.class)))
                .willReturn(transaction);

        //when
        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.sellerId").value(1L))
                .andExpect(jsonPath("$.buyerId").value(2L))
                .andExpect(jsonPath("$.status").value("Pending"))
                .andExpect(jsonPath("$.startedAt").exists());

    }

    @Test
    @DisplayName("Transaction 조회 성공")
    void successGet() throws Exception {
        //given
        Member member1 = createMember(SELLER_ID);
        Member member2 = createMember(BUYER_ID);
        Post salePost = createPost(POST_ID, member1);
        Transaction transaction =
                createTransaction(TRANSACTION_ID, salePost, member2, null,
                        TransactionStatus.PENDING, null, null);

        given(transactionService.get(anyLong()))
                .willReturn(transaction);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.sellerId").value(1L))
                .andExpect(jsonPath("$.buyerId").value(2L))
                .andExpect(jsonPath("$.status").value("Pending"))
                .andExpect(jsonPath("$.startedAt").exists());
    }

    @Test
    @DisplayName("Transaction 모두 조회 성공")
    void successGetAll() throws Exception {
        //given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Member member3 = createMember(3L);
        Member member4 = createMember(BUYER_ID);
        Post salePost1 = createPost(1L, member1);
        Post salePost2 = createPost(2L, member2);
        Post salePost3 = createPost(3L, member3);

        Transaction transaction1 =
                createTransaction(1L, salePost1, member4, null,
                        TransactionStatus.PENDING, null, null);
        Transaction transaction2 =
                createTransaction(2L, salePost2, member4, null,
                        TransactionStatus.PENDING, null, null);
        Transaction transaction3 =
                createTransaction(3L, salePost3, member4, null,
                        TransactionStatus.PENDING, null, null);

        List<Transaction> transactionList =
                Arrays.asList(transaction1, transaction2, transaction3);

        Page<Transaction> transactionPage = new PageImpl<>(transactionList);

        given(transactionService.getAll(anyInt(), anyInt()))
                .willReturn(transactionPage);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value(1L))
                .andExpect(jsonPath("$.content[0].salePostId").value(1L))
                .andExpect(jsonPath("$.content[0].sellerId").value(1L))
                .andExpect(jsonPath("$.content[0].buyerId").value(2L))
                .andExpect(jsonPath("$.content[0].status").value("Pending"))
                .andExpect(jsonPath("$.content[0].startedAt").exists())
                .andExpect(jsonPath("$.content[1].transactionId").value(2L))
                .andExpect(jsonPath("$.content[1].salePostId").value(2L))
                .andExpect(jsonPath("$.content[1].sellerId").value(2L))
                .andExpect(jsonPath("$.content[1].buyerId").value(2L))
                .andExpect(jsonPath("$.content[1].status").value("Pending"))
                .andExpect(jsonPath("$.content[1].startedAt").exists())
                .andExpect(jsonPath("$.content[2].transactionId").value(3L))
                .andExpect(jsonPath("$.content[2].salePostId").value(3L))
                .andExpect(jsonPath("$.content[2].sellerId").value(3L))
                .andExpect(jsonPath("$.content[2].buyerId").value(2L))
                .andExpect(jsonPath("$.content[2].status").value("Pending"))
                .andExpect(jsonPath("$.content[2].startedAt").exists());
    }

    @Test
    @DisplayName("Transaction 의 상태를 Received 로 업데이트 성공")
    void successUpdateToReceived() throws Exception {
        //given
        UpdateStatusToReceivedTransactionRequest transactionRequest =
                UpdateStatusToReceivedTransactionRequest.builder()
                        .transactionId(TRANSACTION_ID)
                        .payAmount(PAY_AMOUNT)
                        .build();

        Member member1 = createMember(SELLER_ID);
        Member member2 = createMember(BUYER_ID);
        Post salePost = createPost(POST_ID, member1);
        Transaction transaction =
                createTransaction(TRANSACTION_ID, salePost, member2, 10000,
                        TransactionStatus.RECEIVED, LocalDateTime.now(), LocalDateTime.now());

        given(transactionUpdateService.updateToReceived(transactionRequest))
                .willReturn(transaction);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.sellerId").value(1L))
                .andExpect(jsonPath("$.buyerId").value(2L))
                .andExpect(jsonPath("$.payAmount").value(10000))
                .andExpect(jsonPath("$.status").value("Received"))
                .andExpect(jsonPath("$.receivedAt").exists())
                .andExpect(jsonPath("$.startedAt").exists());
    }

    @Test
    @DisplayName("Transaction 의 상태를 Completed 로 업데이트 성공")
    void successUpdateToCompleted() throws Exception {
        //given
        UpdateStatusTransactionRequest transactionRequest =
                UpdateStatusTransactionRequest.builder()
                        .transactionId(TRANSACTION_ID)
                        .build();

        Member member1 = createMember(SELLER_ID);
        Member member2 = createMember(BUYER_ID);
        Post salePost = createPost(POST_ID, member1);
        Transaction transaction =
                createTransaction(TRANSACTION_ID, salePost, member2, 10000,
                        TransactionStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now());

        given(transactionUpdateService.updateToCompleted(transactionRequest))
                .willReturn(transaction);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/completion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.sellerId").value(1L))
                .andExpect(jsonPath("$.buyerId").value(2L))
                .andExpect(jsonPath("$.status").value("Completed"))
                .andExpect(jsonPath("$.startedAt").exists())
                .andExpect(jsonPath("$.endedAt").exists());
    }

    @Test
    @DisplayName("Transaction 의 상태를 Canceled 로 업데이트 성공")
    void successUpdateToCanceled() throws Exception {
        //given
        UpdateStatusTransactionRequest transactionRequest =
                UpdateStatusTransactionRequest.builder()
                        .transactionId(TRANSACTION_ID)
                        .build();

        Member member1 = createMember(SELLER_ID);
        Member member2 = createMember(BUYER_ID);
        Post salePost = createPost(POST_ID, member1);
        Transaction transaction =
                createTransaction(TRANSACTION_ID, salePost, member2, 10000,
                        TransactionStatus.CANCELED, LocalDateTime.now(), LocalDateTime.now());

        given(transactionUpdateService.updateToCanceled(transactionRequest))
                .willReturn(transaction);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.sellerId").value(1L))
                .andExpect(jsonPath("$.buyerId").value(2L))
                .andExpect(jsonPath("$.status").value("Canceled"))
                .andExpect(jsonPath("$.startedAt").exists())
                .andExpect(jsonPath("$.endedAt").exists());
    }

    @Test
    @DisplayName("Transaction 의 상태를 Reported 로 업데이트 성공")
    void successUpdateToReported() throws Exception {
        //given
        UpdateStatusTransactionRequest transactionRequest =
                UpdateStatusTransactionRequest.builder()
                        .transactionId(TRANSACTION_ID)
                        .build();

        Member member1 = createMember(SELLER_ID);
        Member member2 = createMember(BUYER_ID);
        Post salePost = createPost(POST_ID, member1);
        Transaction transaction =
                createTransaction(TRANSACTION_ID, salePost, member2, 10000,
                        TransactionStatus.REPORTED, LocalDateTime.now(), LocalDateTime.now());

        given(transactionUpdateService.updateToReported(transactionRequest))
                .willReturn(transaction);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.sellerId").value(1L))
                .andExpect(jsonPath("$.buyerId").value(2L))
                .andExpect(jsonPath("$.status").value("Reported"))
                .andExpect(jsonPath("$.startedAt").exists())
                .andExpect(jsonPath("$.endedAt").exists());
    }
}
