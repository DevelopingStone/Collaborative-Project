package com.zerobase.oriticket.transaction.controller;

import com.zerobase.oriticket.domain.elasticsearch.transaction.controller.TransactionSearchController;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.service.TransactionSearchService;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionSearchController.class)
public class TransactionSearchControllerTest {

    @MockBean
    private TransactionSearchService transactionSearchService;

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/transactions";

    @Test
    @DisplayName("Status 로 Transaction 검색 성공")
    void successSearchByStatus() throws Exception {
        //given
        TransactionSearchDocument transactionSearchDocument1 =
                TransactionSearchDocument.builder()
                        .transactionId(1L)
                        .salePostId(1L)
                        .sellerName("seller")
                        .buyerName("buyer")
                        .payAmount(10000)
                        .status(TransactionStatus.RECEIVED)
                        .receivedAt(LocalDateTime.now())
                        .startedAt(LocalDateTime.now())
                        .build();

        TransactionSearchDocument transactionSearchDocument2 =
                TransactionSearchDocument.builder()
                        .transactionId(2L)
                        .salePostId(2L)
                        .sellerName("seller")
                        .buyerName("buyer")
                        .payAmount(30000)
                        .status(TransactionStatus.RECEIVED)
                        .receivedAt(LocalDateTime.now())
                        .startedAt(LocalDateTime.now())
                        .build();

        List<TransactionSearchDocument> transactionSearchDocumentList =
                Arrays.asList(transactionSearchDocument1, transactionSearchDocument2);

        Page<TransactionSearchDocument> transactionSearchDocuments =
                new PageImpl<>(transactionSearchDocumentList);

        given(transactionSearchService.searchByStatus(anyString(), anyInt(), anyInt()))
                .willReturn(transactionSearchDocuments);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/search?status=received"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value(1L))
                .andExpect(jsonPath("$.content[0].salePostId").value(1L))
                .andExpect(jsonPath("$.content[0].sellerName").value("seller"))
                .andExpect(jsonPath("$.content[0].buyerName").value("buyer"))
                .andExpect(jsonPath("$.content[0].payAmount").value(10000))
                .andExpect(jsonPath("$.content[0].status").value("Received"))
                .andExpect(jsonPath("$.content[0].receivedAt").exists())
                .andExpect(jsonPath("$.content[0].startedAt").exists())
                .andExpect(jsonPath("$.content[1].transactionId").value(2L))
                .andExpect(jsonPath("$.content[1].salePostId").value(2L))
                .andExpect(jsonPath("$.content[1].sellerName").value("seller"))
                .andExpect(jsonPath("$.content[1].buyerName").value("buyer"))
                .andExpect(jsonPath("$.content[1].payAmount").value(30000))
                .andExpect(jsonPath("$.content[1].status").value("Received"))
                .andExpect(jsonPath("$.content[1].receivedAt").exists())
                .andExpect(jsonPath("$.content[1].startedAt").exists());

    }

}
