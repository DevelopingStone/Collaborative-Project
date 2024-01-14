package com.zerobase.oriticket.transaction.service;

import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.elasticsearch.transaction.entity.TransactionSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.transaction.repository.TransactionSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.dto.RegisterTransactionRequest;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.domain.transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionSearchRepository transactionSearchRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostSearchRepository postSearchRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

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

    private Post createPost(Long salePostId, Member member, Ticket ticket){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(SaleStatus.FOR_SALE)
                .build();
    }

    private Post createPost(Long salePostId){
        return Post.builder()
                .salePostId(salePostId)
                .saleStatus(SaleStatus.FOR_SALE)
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
                .status(TransactionStatus.PENDING)
                .startedAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId, String nickname){
        return Member.builder()
                .memberId(membersId)
                .nickname(nickname)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Transaction 등록 성공")
    void successRegister(){
        //given
        RegisterTransactionRequest registerRequest =
                RegisterTransactionRequest.builder()
                        .salePostId(1L)
                        .memberId(2L)
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(1L, sports, stadium, awayTeam);
        Member member1 = createMember(1L, "seller name");
        Member member2 = createMember(2L, "buyer name");
        Post salePost = createPost(1L, member1, ticket);
        Transaction transaction = createTransaction(10L, salePost, member2);

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(salePost));
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member2));
        given(transactionRepository.validateCanRegisterTransaction(any(Post.class)))
                .willReturn(true);
        given(transactionRepository.save(any(Transaction.class)))
                .willReturn(transaction);

        ArgumentCaptor<TransactionSearchDocument> transactionDocumentCaptor =
                ArgumentCaptor.forClass(TransactionSearchDocument.class);

        ArgumentCaptor<Post> salePostCaptor = ArgumentCaptor.forClass(Post.class);

        //when
        Transaction fetchedTransaction = transactionService.register(registerRequest);

        //then
        then(transactionSearchRepository).should(times(1)).save(transactionDocumentCaptor.capture());
        then(postRepository).should(times(1)).save(salePostCaptor.capture());
        TransactionSearchDocument fetchedTransactionDocument = transactionDocumentCaptor.getValue();
        Post fetchedSalePost = salePostCaptor.getValue();

        assertThat(fetchedTransaction.getTransactionId()).isEqualTo(10L);
        assertThat(fetchedTransaction.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedTransaction.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertNotNull(fetchedTransaction.getStartedAt());

        assertThat(fetchedTransactionDocument.getTransactionId()).isEqualTo(10L);
        assertThat(fetchedTransactionDocument.getSalePostId()).isEqualTo(1L);
        assertThat(fetchedTransactionDocument.getSellerName()).isEqualTo("seller name");
        assertThat(fetchedTransactionDocument.getBuyerName()).isEqualTo("buyer name");
        assertThat(fetchedTransactionDocument.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertNotNull(fetchedTransactionDocument.getStartedAt());

        assertThat(fetchedSalePost.getSaleStatus()).isEqualTo(SaleStatus.TRADING);
    }

    @Test
    @DisplayName("Transaction 조회 성공")
    void successGet(){
        //given
        Member member = createMember(3L, "buyer name");
        Post salePost = createPost(10L);
        Transaction transaction = createTransaction(15L, salePost, member);

        given(transactionRepository.findById(anyLong()))
                .willReturn(Optional.of(transaction));

        //when
        Transaction fetchedTransaction = transactionService.get(15L);

        //then
        assertThat(fetchedTransaction.getTransactionId()).isEqualTo(15L);
        assertThat(fetchedTransaction.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedTransaction.getMember().getMemberId()).isEqualTo(3L);
        assertThat(fetchedTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertNotNull(fetchedTransaction.getStartedAt());

    }

    @Test
    @DisplayName("모든 Transaction 조회 성공")
    void successGetAll(){
        //given
        Member member1 = createMember(3L, "buyer1 name");
        Member member2 = createMember(4L, "buyer2 name");
        Post salePost1 = createPost(10L);
        Post salePost2 = createPost(11L);
        Transaction transaction1 = createTransaction(15L, salePost1, member1);
        Transaction transaction2 = createTransaction(16L, salePost2, member2);

        List<Transaction> transactionList = Arrays.asList(transaction1, transaction2);

        given(transactionRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<>(transactionList));

        //when
        Page<Transaction> fetchedTransactions = transactionService.getAll(1, 10);

        //then
        assertThat(fetchedTransactions.getContent().get(0).getTransactionId()).isEqualTo(15L);
        assertThat(fetchedTransactions.getContent().get(0).getSalePost()).isEqualTo(salePost1);
        assertThat(fetchedTransactions.getContent().get(0).getMember().getMemberId()).isEqualTo(3L);
        assertThat(fetchedTransactions.getContent().get(0).getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertNotNull(fetchedTransactions.getContent().get(0).getStartedAt());
        assertThat(fetchedTransactions.getContent().get(1).getTransactionId()).isEqualTo(16L);
        assertThat(fetchedTransactions.getContent().get(1).getSalePost()).isEqualTo(salePost2);
        assertThat(fetchedTransactions.getContent().get(1).getMember().getMemberId()).isEqualTo(4L);
        assertThat(fetchedTransactions.getContent().get(1).getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertNotNull(fetchedTransactions.getContent().get(1).getStartedAt());

    }


}
