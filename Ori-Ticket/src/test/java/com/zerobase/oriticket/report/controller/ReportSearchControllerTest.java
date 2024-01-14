package com.zerobase.oriticket.report.controller;

import com.zerobase.oriticket.domain.elasticsearch.post.dto.PostSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.controller.ReportSearchController;
import com.zerobase.oriticket.domain.elasticsearch.report.dto.ReportPostSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.dto.ReportTransactionSearchResponse;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportPostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.entity.ReportTransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.report.service.ReportPostSearchService;
import com.zerobase.oriticket.domain.elasticsearch.report.service.ReportTransactionSearchService;
import com.zerobase.oriticket.domain.elasticsearch.transaction.dto.TransactionSearchResponse;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.constants.ReportTransactionType;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportSearchController.class)
public class ReportSearchControllerTest {

    @MockBean
    private ReportPostSearchService reportPostSearchService;

    @MockBean
    private ReportTransactionSearchService reportTransactionSearchService;

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/report";

    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 20000;
    private final static boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "seat info";
    private final static String IMG_URL = "image url";
    private final static String NOTE = "note";

    private PostSearchResponse createPostSearchResponse(
            Long salePostId,
            String memberName,
            String sportsName,
            String stadiumName,
            String homeTeamName,
            String awayTeamName
    ){
        return PostSearchResponse.builder()
                .salePostId(salePostId)
                .memberName(memberName)
                .sportsName(sportsName)
                .stadiumName(stadiumName)
                .homeTeamName(homeTeamName)
                .awayTeamName(awayTeamName)
                .quantity(QUANTITY)
                .salePrice(SALE_PRICE)
                .originalPrice(ORIGINAL_PRICE)
                .expirationAt(LocalDateTime.now().plusDays(1))
                .isSuccessive(IS_SUCCESSIVE)
                .seatInfo(SEAT_INFO)
                .imgUrl(IMG_URL)
                .note(NOTE)
                .saleStatus(SaleStatus.FOR_SALE.toString())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private TransactionSearchResponse createTransactionSearchResponse(
            Long transactionId,
            Long salePostId,
            String sellerName,
            String buyerName,
            Integer payAmount,
            String status
    ){
        return TransactionSearchResponse.builder()
                .transactionId(transactionId)
                .salePostId(salePostId)
                .sellerName(sellerName)
                .buyerName(buyerName)
                .payAmount(payAmount)
                .status(status)
                .receivedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now())
                .endedAt(LocalDateTime.now())
                .build();
    }

    private ReportPostSearchResponse createReportPostSearchResponse(
            Long reportPostId,
            String memberName,
            PostSearchResponse salPost,
            String note
    ){
        return ReportPostSearchResponse.builder()
                .reportPostId(reportPostId)
                .memberName(memberName)
                .salePost(salPost)
                .reason(ReportPostType.INAPPROPRIATE_PHOTO.getReportType())
                .reportedAt(LocalDateTime.now())
                .status(ReportReactStatus.REACTED)
                .reactedAt(LocalDateTime.now())
                .note(note)
                .build();
    }

    private ReportTransactionSearchResponse createReportTransactionSearchResponse(
            Long reportTransactionId,
            String memberName,
            TransactionSearchResponse transaction,
            String note
    ){
        return ReportTransactionSearchResponse.builder()
                .reportTransactionId(reportTransactionId)
                .memberName(memberName)
                .transaction(transaction)
                .reason(ReportTransactionType.ECONOMIC_LOSS.getReportType())
                .reportedAt(LocalDateTime.now())
                .status(ReportReactStatus.REACTED)
                .reactedAt(LocalDateTime.now())
                .note(note)
                .build();
    }

    private ReportPostSearchDocument createReportPostSearchDocument(
            Long reportPostId,
            String memberName,
            Long salePostId,
            String reason,
            ReportReactStatus status
    ){
        return ReportPostSearchDocument.builder()
                .reportPostId(reportPostId)
                .memberName(memberName)
                .salePostId(salePostId)
                .reason(reason)
                .reportedAt(LocalDateTime.now())
                .status(status)
                .build();
    }

    private ReportTransactionSearchDocument createReportTransactionSearchDocument(
            Long reportTransactionId,
            String memberName,
            Long transactionId,
            String reason,
            ReportReactStatus status
    ){
        return ReportTransactionSearchDocument.builder()
                .reportTransactionId(reportTransactionId)
                .memberName(memberName)
                .transactionId(transactionId)
                .reason(reason)
                .reportedAt(LocalDateTime.now())
                .status(status)
                .build();
    }

    @Test
    @DisplayName("Report Post 검색 성공")
    void successReportPost() throws Exception {
        //given
        PostSearchResponse postSearchResponse =
                createPostSearchResponse(10L, "seller name", "야구",
                        "고척돔", "키움", "한화");
        ReportPostSearchDocument reportPostSearchDocument =
                createReportPostSearchDocument(1L, "first member", 10L,
                        ReportPostType.OTHER_ISSUES.getReportType(), ReportReactStatus.PROCESSING);
        ReportPostSearchResponse reportPostSearchResponse =
                createReportPostSearchResponse(1L, "first member",
                        postSearchResponse, "react note");

        List<ReportPostSearchDocument> reportPostDocumentList =
                List.of(reportPostSearchDocument);
        Page<ReportPostSearchDocument> reportPostDocuments =
                new PageImpl<>(reportPostDocumentList);

        given(reportPostSearchService.search(anyString(), anyInt(), anyInt()))
                .willReturn(reportPostDocuments);
        given(reportPostSearchService.entityToDto(any(ReportPostSearchDocument.class)))
                .willReturn(reportPostSearchResponse);


        //when
        //then
        mockMvc.perform(get(BASE_URL+"/posts/search?name=member"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reportPostId").value(1L))
                .andExpect(jsonPath("$.content[0].memberName").value("first member"))
                .andExpect(jsonPath("$.content[0].salePost.salePostId").value(10L))
                .andExpect(jsonPath("$.content[0].reason").value("Inappropriate photo"))
                .andExpect(jsonPath("$.content[0].status").value("REACTED"));

    }

    @Test
    @DisplayName("Report Post 검색 성공")
    void successReportTransaction() throws Exception {
        //given
        TransactionSearchResponse transactionSearchResponse =
                createTransactionSearchResponse(10L, 1L, "seller name",
                        "buyer name", 10000, TransactionStatus.RECEIVED.getState());
        ReportTransactionSearchDocument reportTransactionSearchDocument =
                createReportTransactionSearchDocument(1L, "first member", 10L,
                        ReportPostType.OTHER_ISSUES.getReportType(), ReportReactStatus.PROCESSING);
        ReportTransactionSearchResponse reportTransactionSearchResponse =
                createReportTransactionSearchResponse(1L, "first member",
                        transactionSearchResponse, "react note");

        List<ReportTransactionSearchDocument> reportTransactionDocumentList =
                List.of(reportTransactionSearchDocument);
        Page<ReportTransactionSearchDocument> reportTransactionDocuments =
                new PageImpl<>(reportTransactionDocumentList);

        given(reportTransactionSearchService.search(anyString(), anyInt(), anyInt()))
                .willReturn(reportTransactionDocuments);
        given(reportTransactionSearchService.entityToDto(any(ReportTransactionSearchDocument.class)))
                .willReturn(reportTransactionSearchResponse);


        //when
        //then
        mockMvc.perform(get(BASE_URL+"/transactions/search?name=member"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reportTransactionId").value(1L))
                .andExpect(jsonPath("$.content[0].memberName").value("first member"))
                .andExpect(jsonPath("$.content[0].transaction.transactionId").value(10L))
                .andExpect(jsonPath("$.content[0].reason").value("Economic loss"))
                .andExpect(jsonPath("$.content[0].status").value("REACTED"));

    }
}
