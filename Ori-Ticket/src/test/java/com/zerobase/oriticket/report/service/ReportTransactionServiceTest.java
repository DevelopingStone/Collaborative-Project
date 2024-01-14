package com.zerobase.oriticket.report.service;

import com.zerobase.oriticket.domain.elasticsearch.report.repository.ReportTransactionSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.constants.ReportTransactionType;
import com.zerobase.oriticket.domain.report.dto.RegisterReportTransactionRequest;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import com.zerobase.oriticket.domain.report.repository.ReportTransactionRepository;
import com.zerobase.oriticket.domain.report.service.ReportTransactionService;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReportTransactionServiceTest {

    @Mock
    private ReportTransactionRepository reportTransactionRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ReportTransactionSearchRepository reportTransactionSearchRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportTransactionService reportTransactionService;

    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 20000;
    private final static boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "seat info";
    private final static String IMG_URL = "image url";
    private final static String NOTE = "note";

    private Sports createSports(Long sportsId, String sportsName){
        return Sports.builder()
                .sportsId(sportsId)
                .sportsName(sportsName)
                .build();
    }

    private Stadium createStadium(Long stadiumId, Sports sports, String stadiumName, String homeTeamName){
        return Stadium.builder()
                .stadiumId(stadiumId)
                .sports(sports)
                .stadiumName(stadiumName)
                .homeTeamName(homeTeamName)
                .build();
    }

    private AwayTeam createAwayTeam(Long awayTeamId, Sports sports, String awayTeamName){
        return AwayTeam.builder()
                .awayTeamId(awayTeamId)
                .sports(sports)
                .awayTeamName(awayTeamName)
                .build();
    }

    private Ticket createTicket(Long ticketId, Sports sports, Stadium stadium, AwayTeam awayTeam){
        return Ticket.builder()
                .ticketId(ticketId)
                .sports(sports)
                .stadium(stadium)
                .awayTeam(awayTeam)
                .quantity(QUANTITY)
                .salePrice(SALE_PRICE)
                .originalPrice(ORIGINAL_PRICE)
                .expirationAt(LocalDateTime.now().plusDays(5))
                .isSuccessive(IS_SUCCESSIVE)
                .seatInfo(SEAT_INFO)
                .imgUrl(IMG_URL)
                .note(NOTE)
                .build();
    }

    private Post createPost(Long salePostId, Member member, Ticket ticket, SaleStatus status){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(status)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Transaction createTransaction(
            Long transactionId,
            Post salePost,
            Member member
    ){
        return Transaction.builder()
                .transactionId(transactionId)
                .salePost(salePost)
                .member(member)
                .startedAt(LocalDateTime.now())
                .status(TransactionStatus.PENDING)
                .build();
    }

    private ReportTransaction createReportTransaction(
            Long reportTransactionId,
            Member member,
            Transaction transaction,
            ReportReactStatus status,
            LocalDateTime reactedAt,
            String note
    ){
        return ReportTransaction.builder()
                .reportTransactionId(reportTransactionId)
                .member(member)
                .transaction(transaction)
                .reason(ReportTransactionType.ECONOMIC_LOSS)
                .reportedAt(LocalDateTime.now())
                .status(status)
                .reactedAt(reactedAt)
                .note(note)
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("Report Transaction 등록 성공")
    void successRegister() {
        //given
        RegisterReportTransactionRequest registerRequest =
                RegisterReportTransactionRequest.builder()
                        .memberId(2L)
                        .reason("Economic_loss")
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "두산");
        Ticket ticket = createTicket(10L, sports, stadium, awayTeam);
        Member member1 = createMember(11L);
        Member member2 = createMember(2L);
        Post salePost = createPost(14L, member1, ticket, SaleStatus.FOR_SALE);
        Transaction transaction = createTransaction(19L, salePost, member2);
        ReportTransaction reportTransaction = createReportTransaction(5L, member2, transaction,
                        ReportReactStatus.PROCESSING, null, null);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member2));
        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(transaction));
        given(reportTransactionRepository.save(any(ReportTransaction.class)))
                .willReturn(reportTransaction);

        //when
        ReportTransaction fetchedReportTransaction = reportTransactionService.register(19L, registerRequest);

        //then
        assertThat(fetchedReportTransaction.getReportTransactionId()).isEqualTo(5L);
        assertThat(fetchedReportTransaction.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedReportTransaction.getTransaction()).isEqualTo(transaction);
        assertThat(fetchedReportTransaction.getReason()).isEqualTo(ReportTransactionType.ECONOMIC_LOSS);
        assertNotNull(fetchedReportTransaction.getReportedAt());
        assertThat(fetchedReportTransaction.getStatus()).isEqualTo(ReportReactStatus.PROCESSING);
        assertNull(fetchedReportTransaction.getReactedAt());
        assertNull(fetchedReportTransaction.getNote());
    }

    @Test
    @Transactional
    @DisplayName("Report Transaction 처리 성공")
    void successUpdateToReacted() {
        //given
        UpdateReportRequest updateRequest =
                UpdateReportRequest.builder()
                        .note("react note")
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "두산");
        Ticket ticket = createTicket(10L, sports, stadium, awayTeam);
        Member member1 = createMember(11L);
        Member member2 = createMember(2L);
        Post salePost = createPost(14L, member1, ticket, SaleStatus.FOR_SALE);
        Transaction transaction = createTransaction(19L, salePost, member2);
        ReportTransaction reportTransaction = createReportTransaction(5L, member2, transaction,
                        ReportReactStatus.REACTED, LocalDateTime.now(), "react note");

        given(reportTransactionRepository.findById(anyLong()))
                .willReturn(Optional.of(reportTransaction));
        given(reportTransactionRepository.save(any(ReportTransaction.class)))
                .willReturn(reportTransaction);

        //when
        ReportTransaction fetchedReportTransaction =
                reportTransactionService.updateToReacted(5L, updateRequest);

        //then
        assertThat(fetchedReportTransaction.getReportTransactionId()).isEqualTo(5L);
        assertThat(fetchedReportTransaction.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedReportTransaction.getTransaction()).isEqualTo(transaction);
        assertThat(fetchedReportTransaction.getReason()).isEqualTo(ReportTransactionType.ECONOMIC_LOSS);
        assertNotNull(fetchedReportTransaction.getReportedAt());
        assertThat(fetchedReportTransaction.getStatus()).isEqualTo(ReportReactStatus.REACTED);
        assertNotNull(fetchedReportTransaction.getReactedAt());
        assertThat(fetchedReportTransaction.getNote()).isEqualTo("react note");

    }
}
