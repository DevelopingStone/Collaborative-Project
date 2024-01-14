package com.zerobase.oriticket.transaction.service;

import com.zerobase.oriticket.domain.chat.repository.ChatRoomRepository;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.repository.TransactionSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusToReceivedTransactionRequest;
import com.zerobase.oriticket.domain.transaction.dto.UpdateStatusTransactionRequest;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.domain.transaction.service.TransactionUpdateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TransactionUpdateServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostSearchRepository postSearchRepository;

    @Mock
    private TransactionSearchRepository transactionSearchRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private TransactionUpdateService transactionUpdateService;

    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 15000;
    private final static Boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "A열 4석";
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

    private Post createPost(Long salePostId, Member member, SaleStatus status, Ticket ticket){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(status)
                .build();
    }

    private Post createPost(Long salePostId, Member member, SaleStatus status){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .saleStatus(status)
                .build();
    }

    private Member createMember(Long membersId, String nickname){
        return Member.builder()
                .memberId(membersId)
                .nickname(nickname)
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
    @Transactional
    @DisplayName("Transaction 상태 Received 로 업데이트 성공")
    void successUpdateToReceived(){
        //given
        UpdateStatusToReceivedTransactionRequest updateRequest =
                UpdateStatusToReceivedTransactionRequest.builder()
                        .transactionId(11L)
                        .payAmount(10000)
                        .build();
        Member member1 = createMember(1L, "seller name");
        Member member2 = createMember(2L, "buyer name");
        Post salePost = createPost(1L, member1, SaleStatus.TRADING);
        Transaction transaction = createTransaction(11L, salePost, member2,
                null, TransactionStatus.PENDING, null, null);

        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(transaction));

        given(transactionRepository.save(any(Transaction.class)))
                .willReturn(transaction);

        ArgumentCaptor<TransactionSearchDocument> transactionDocumentCaptor =
                ArgumentCaptor.forClass(TransactionSearchDocument.class);

        //when
        Transaction fetchedTransaction = transactionUpdateService.updateToReceived(updateRequest);

        //then
        then(transactionSearchRepository).should(times(1)).save(transactionDocumentCaptor.capture());
        TransactionSearchDocument fetchedTransactionDocument = transactionDocumentCaptor.getValue();

        assertThat(fetchedTransaction.getTransactionId()).isEqualTo(11L);
        assertThat(fetchedTransaction.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedTransaction.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedTransaction.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransaction.getStatus()).isEqualTo(TransactionStatus.RECEIVED);
        assertNotNull(fetchedTransaction.getReceivedAt());
        assertNotNull(fetchedTransaction.getStartedAt());

        assertThat(fetchedTransactionDocument.getTransactionId()).isEqualTo(11L);
        assertThat(fetchedTransactionDocument.getSalePostId()).isEqualTo(1L);
        assertThat(fetchedTransactionDocument.getSellerName()).isEqualTo("seller name");
        assertThat(fetchedTransactionDocument.getBuyerName()).isEqualTo("buyer name");
        assertThat(fetchedTransactionDocument.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactionDocument.getStatus()).isEqualTo(TransactionStatus.RECEIVED);
        assertNotNull(fetchedTransactionDocument.getReceivedAt());
        assertNotNull(fetchedTransactionDocument.getStartedAt());
    }

    @Test
    @Transactional
    @DisplayName("Transaction 상태 Completed 로 업데이트 성공")
    void successUpdateToCompleted(){
        //given
        UpdateStatusTransactionRequest updateRequest =
                UpdateStatusTransactionRequest.builder()
                        .transactionId(12L)
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(1L, sports, stadium, awayTeam);
        Member member1 = createMember(2L, "seller name");
        Member member2 = createMember(3L, "buyer name");
        Post salePost = createPost(2L, member1, SaleStatus.TRADING, ticket);
        Transaction transaction = createTransaction(12L, salePost, member2,
                10000, TransactionStatus.RECEIVED, LocalDateTime.now(), null);

        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(transaction));

        given(transactionRepository.save(any(Transaction.class)))
                .willReturn(transaction);

        ArgumentCaptor<TransactionSearchDocument> transactionDocumentCaptor =
                ArgumentCaptor.forClass(TransactionSearchDocument.class);
        ArgumentCaptor<Post> salePostCaptor = ArgumentCaptor.forClass(Post.class);

        //when
        Transaction fetchedTransaction = transactionUpdateService.updateToCompleted(updateRequest);

        //then
        then(transactionSearchRepository).should(times(1)).save(transactionDocumentCaptor.capture());
        then(postRepository).should(times(1)).save(salePostCaptor.capture());
        TransactionSearchDocument fetchedTransactionDocument = transactionDocumentCaptor.getValue();
        Post fetchedSalePost = salePostCaptor.getValue();

        assertThat(fetchedTransaction.getTransactionId()).isEqualTo(12L);
        assertThat(fetchedTransaction.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedTransaction.getMember().getMemberId()).isEqualTo(3L);
        assertThat(fetchedTransaction.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertNotNull(fetchedTransaction.getReceivedAt());
        assertNotNull(fetchedTransaction.getStartedAt());
        assertNotNull(fetchedTransaction.getEndedAt());

        assertThat(fetchedTransactionDocument.getTransactionId()).isEqualTo(12L);
        assertThat(fetchedTransactionDocument.getSalePostId()).isEqualTo(2L);
        assertThat(fetchedTransactionDocument.getSellerName()).isEqualTo("seller name");
        assertThat(fetchedTransactionDocument.getBuyerName()).isEqualTo("buyer name");
        assertThat(fetchedTransactionDocument.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactionDocument.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertNotNull(fetchedTransactionDocument.getReceivedAt());
        assertNotNull(fetchedTransactionDocument.getStartedAt());
        assertNotNull(fetchedTransactionDocument.getEndedAt());

        assertThat(fetchedSalePost.getSaleStatus()).isEqualTo(SaleStatus.SOLD);
    }

    @Test
    @Transactional
    @DisplayName("Transaction 상태 Canceled 로 업데이트 성공")
    void successUpdateToCanceled(){
        //given
        UpdateStatusTransactionRequest updateRequest =
                UpdateStatusTransactionRequest.builder()
                        .transactionId(12L)
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(1L, sports, stadium, awayTeam);
        Member member1 = createMember(2L, "seller name");
        Member member2 = createMember(3L, "buyer name");
        Post salePost = createPost(2L, member1, SaleStatus.TRADING, ticket);
        Transaction transaction = createTransaction(12L, salePost, member2,
                10000, TransactionStatus.RECEIVED, LocalDateTime.now(), null);

        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(transaction));

        given(transactionRepository.save(any(Transaction.class)))
                .willReturn(transaction);

        ArgumentCaptor<TransactionSearchDocument> transactionDocumentCaptor =
                ArgumentCaptor.forClass(TransactionSearchDocument.class);
        ArgumentCaptor<Post> salePostCaptor = ArgumentCaptor.forClass(Post.class);

        //when
        Transaction fetchedTransaction = transactionUpdateService.updateToCanceled(updateRequest);

        //then
        then(transactionSearchRepository).should(times(1)).save(transactionDocumentCaptor.capture());
        then(postRepository).should(times(1)).save(salePostCaptor.capture());
        TransactionSearchDocument fetchedTransactionDocument = transactionDocumentCaptor.getValue();
        Post fetchedSalePost = salePostCaptor.getValue();

        assertThat(fetchedTransaction.getTransactionId()).isEqualTo(12L);
        assertThat(fetchedTransaction.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedTransaction.getMember().getMemberId()).isEqualTo(3L);
        assertThat(fetchedTransaction.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransaction.getStatus()).isEqualTo(TransactionStatus.CANCELED);
        assertNotNull(fetchedTransaction.getReceivedAt());
        assertNotNull(fetchedTransaction.getStartedAt());
        assertNotNull(fetchedTransaction.getEndedAt());

        assertThat(fetchedTransactionDocument.getTransactionId()).isEqualTo(12L);
        assertThat(fetchedTransactionDocument.getSalePostId()).isEqualTo(2L);
        assertThat(fetchedTransactionDocument.getSellerName()).isEqualTo("seller name");
        assertThat(fetchedTransactionDocument.getBuyerName()).isEqualTo("buyer name");
        assertThat(fetchedTransactionDocument.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactionDocument.getStatus()).isEqualTo(TransactionStatus.CANCELED);
        assertNotNull(fetchedTransactionDocument.getReceivedAt());
        assertNotNull(fetchedTransactionDocument.getStartedAt());
        assertNotNull(fetchedTransactionDocument.getEndedAt());

        assertThat(fetchedSalePost.getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
    }

    @Test
    @Transactional
    @DisplayName("Transaction 상태 Reported 로 업데이트 성공")
    void successUpdateToReported(){
        //given
        UpdateStatusTransactionRequest updateRequest =
                UpdateStatusTransactionRequest.builder()
                        .transactionId(12L)
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(1L, sports, stadium, awayTeam);
        Member member1 = createMember(2L, "seller name");
        Member member2 = createMember(3L, "buyer name");
        Post salePost = createPost(2L, member1, SaleStatus.TRADING, ticket);
        Transaction transaction = createTransaction(12L, salePost, member2,
                10000, TransactionStatus.RECEIVED, LocalDateTime.now(), null);

        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(transaction));

        given(transactionRepository.save(any(Transaction.class)))
                .willReturn(transaction);

        ArgumentCaptor<TransactionSearchDocument> transactionDocumentCaptor =
                ArgumentCaptor.forClass(TransactionSearchDocument.class);
        ArgumentCaptor<Post> salePostCaptor = ArgumentCaptor.forClass(Post.class);

        //when
        Transaction fetchedTransaction = transactionUpdateService.updateToReported(updateRequest);

        //then
        then(transactionSearchRepository).should(times(1)).save(transactionDocumentCaptor.capture());
        then(postRepository).should(times(1)).save(salePostCaptor.capture());
        TransactionSearchDocument fetchedTransactionDocument = transactionDocumentCaptor.getValue();
        Post fetchedSalePost = salePostCaptor.getValue();

        assertThat(fetchedTransaction.getTransactionId()).isEqualTo(12L);
        assertThat(fetchedTransaction.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedTransaction.getMember().getMemberId()).isEqualTo(3L);
        assertThat(fetchedTransaction.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransaction.getStatus()).isEqualTo(TransactionStatus.REPORTED);
        assertNotNull(fetchedTransaction.getReceivedAt());
        assertNotNull(fetchedTransaction.getStartedAt());
        assertNotNull(fetchedTransaction.getEndedAt());

        assertThat(fetchedTransactionDocument.getTransactionId()).isEqualTo(12L);
        assertThat(fetchedTransactionDocument.getSalePostId()).isEqualTo(2L);
        assertThat(fetchedTransactionDocument.getSellerName()).isEqualTo("seller name");
        assertThat(fetchedTransactionDocument.getBuyerName()).isEqualTo("buyer name");
        assertThat(fetchedTransactionDocument.getPayAmount()).isEqualTo(10000);
        assertThat(fetchedTransactionDocument.getStatus()).isEqualTo(TransactionStatus.REPORTED);
        assertNotNull(fetchedTransactionDocument.getReceivedAt());
        assertNotNull(fetchedTransactionDocument.getStartedAt());
        assertNotNull(fetchedTransactionDocument.getEndedAt());

        assertThat(fetchedSalePost.getSaleStatus()).isEqualTo(SaleStatus.REPORTED);
    }

}
