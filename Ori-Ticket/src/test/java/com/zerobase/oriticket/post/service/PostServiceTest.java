package com.zerobase.oriticket.post.service;

import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.dto.RegisterPostRequest;
import com.zerobase.oriticket.domain.post.dto.UpdateStatusToReportedPostRequest;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.*;
import com.zerobase.oriticket.domain.post.service.PostService;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
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
public class PostServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostSearchRepository postSearchRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SportsRepository sportsRepository;

    @Mock
    private StadiumRepository stadiumRepository;

    @Mock
    private AwayTeamRepository awayTeamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;
    
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

    private RegisterPostRequest createRegisterPostRequest(
            Long memberId,
            Long sportsId,
            Long stadiumId,
            Long awayTeamId
    ){
        return RegisterPostRequest.builder()
                .memberId(memberId)
                .sportsId(sportsId)
                .stadiumId(stadiumId)
                .awayTeamId(awayTeamId)
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
                .createdAt(LocalDateTime.now())
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
    @DisplayName("Post 등록 성공")
    void successRegisterPost(){
        //given
        RegisterPostRequest registerRequest = createRegisterPostRequest(11L, 1L, 2L, 1L);
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(2L, sports, "잠실 주경기장", "두산");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(1L, sports, stadium, awayTeam);
        Member member = createMember(11L, "seller name");
        Post post = createPost(10L, member, ticket);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(sportsRepository.findById(anyLong()))
                .willReturn(Optional.of(sports));
        given(stadiumRepository.findById(anyLong()))
                .willReturn(Optional.of(stadium));
        given(awayTeamRepository.findById(anyLong()))
                .willReturn(Optional.of(awayTeam));
        given(ticketRepository.save(any(Ticket.class)))
                .willReturn(ticket);
        given(postRepository.save(any(Post.class)))
                .willReturn(post);

        ArgumentCaptor<PostSearchDocument> captor = ArgumentCaptor.forClass(PostSearchDocument.class);

        //when
        Post fetchedPost = postService.registerPost(registerRequest);

        //then
        then(postSearchRepository).should(times(1)).save(captor.capture());
        PostSearchDocument fetchedPostDocument = captor.getValue();

        assertThat(fetchedPostDocument.getSalePostId()).isEqualTo(10L);
        assertThat(fetchedPostDocument.getMemberName()).isEqualTo("seller name");
        assertThat(fetchedPostDocument.getSportsName()).isEqualTo("야구");
        assertThat(fetchedPostDocument.getStadiumName()).isEqualTo("잠실 주경기장");
        assertThat(fetchedPostDocument.getHomeTeamName()).isEqualTo("두산");
        assertThat(fetchedPostDocument.getAwayTeamName()).isEqualTo("한화");
        assertThat(fetchedPostDocument.getQuantity()).isEqualTo(1);
        assertThat(fetchedPostDocument.getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPostDocument.getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPostDocument.getExpirationAt());
        assertThat(fetchedPostDocument.getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPostDocument.getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPostDocument.getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPostDocument.getNote()).isEqualTo("note");
        assertThat(fetchedPostDocument.getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
        assertNotNull(fetchedPostDocument.getCreatedAt());

        assertThat(fetchedPost.getSalePostId()).isEqualTo(10L);
        assertThat(fetchedPost.getMember().getMemberId()).isEqualTo(11L);
        assertThat(fetchedPost.getTicket()).isEqualTo(ticket);
        assertThat(fetchedPost.getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
        assertNotNull(fetchedPost.getCreatedAt());
    }

    @Test
    @DisplayName("Post 조회 성공")
    void successGet(){
        //given
        Sports sports = createSports(2L, "농구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(3L, sports, "기아");
        Ticket ticket = createTicket(11L, sports, stadium, awayTeam);
        Member member = createMember(2L, "seller name");
        Post post = createPost(3L, member, ticket);

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));
        //when
        Post fetchedPost = postService.get(3L);

        //then
        assertThat(fetchedPost.getSalePostId()).isEqualTo(3L);
        assertThat(fetchedPost.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedPost.getTicket()).isEqualTo(ticket);
        assertThat(fetchedPost.getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
        assertNotNull(fetchedPost.getCreatedAt());
    }

    @Test
    @Transactional
    @DisplayName("Post 삭제 성공")
    void successDelete(){
        //given
        Sports sports = createSports(2L, "농구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(3L, sports, "기아");
        Ticket ticket = createTicket(11L, sports, stadium, awayTeam);
        Member member = createMember(2L, "seller name");
        Post post = createPost(3L, member, ticket);

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        ArgumentCaptor<PostSearchDocument> postDocumentCaptor = ArgumentCaptor.forClass(PostSearchDocument.class);

        //when
        Long fetchedPostId = postService.delete(3L);

        //then
        then(postRepository).should(times(1)).delete(postCaptor.capture());
        then(ticketRepository).should(times(1)).delete(ticketCaptor.capture());
        then(postSearchRepository).should(times(1)).delete(postDocumentCaptor.capture());
        Post fetchedPost = postCaptor.getValue();
        Ticket fetchedTicket = ticketCaptor.getValue();
        PostSearchDocument fetchedPostDocument = postDocumentCaptor.getValue();

        assertThat(fetchedPostId).isEqualTo(3L);

        assertThat(fetchedPost.getSalePostId()).isEqualTo(3L);
        assertThat(fetchedPost.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedPost.getTicket()).isEqualTo(ticket);
        assertThat(fetchedPost.getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
        assertNotNull(fetchedPost.getCreatedAt());

        assertThat(fetchedTicket.getTicketId()).isEqualTo(11L);
        assertThat(fetchedTicket.getSports()).isEqualTo(sports);
        assertThat(fetchedTicket.getStadium()).isEqualTo(stadium);
        assertThat(fetchedTicket.getAwayTeam()).isEqualTo(awayTeam);
        assertThat(fetchedTicket.getQuantity()).isEqualTo(1);
        assertThat(fetchedTicket.getSalePrice()).isEqualTo(10000);
        assertThat(fetchedTicket.getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedTicket.getExpirationAt());
        assertThat(fetchedTicket.getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedTicket.getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedTicket.getImgUrl()).isEqualTo("image url");
        assertThat(fetchedTicket.getNote()).isEqualTo("note");

        assertThat(fetchedPostDocument.getSalePostId()).isEqualTo(3L);
        assertThat(fetchedPostDocument.getMemberName()).isEqualTo("seller name");
        assertThat(fetchedPostDocument.getStadiumName()).isEqualTo("고척돔");
        assertThat(fetchedPostDocument.getHomeTeamName()).isEqualTo("키움");
        assertThat(fetchedPostDocument.getAwayTeamName()).isEqualTo("기아");
        assertThat(fetchedPostDocument.getQuantity()).isEqualTo(1);
        assertThat(fetchedPostDocument.getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPostDocument.getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPostDocument.getExpirationAt());
        assertThat(fetchedPostDocument.getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPostDocument.getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPostDocument.getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPostDocument.getNote()).isEqualTo("note");
        assertThat(fetchedPostDocument.getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
        assertNotNull(fetchedPostDocument.getCreatedAt());
    }

    @Test
    @DisplayName("Post 상태 Reported 로 업데이트 성공")
    void successUpdateToReported(){
        //given
        UpdateStatusToReportedPostRequest updateRequest =
                UpdateStatusToReportedPostRequest.builder()
                        .salePostId(3L)
                        .build();

        Sports sports = createSports(2L, "농구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(3L, sports, "기아");
        Ticket ticket = createTicket(11L, sports, stadium, awayTeam);
        Member member = createMember(2L, "seller name");
        Post post = createPost(3L, member, ticket);
        Post reportedPost = createPost(3L, member, ticket);
        reportedPost.setSaleStatus(SaleStatus.REPORTED);

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class)))
                .willReturn(reportedPost);

        ArgumentCaptor<PostSearchDocument> captor =
                ArgumentCaptor.forClass(PostSearchDocument.class);

        //when
        Post fetchedPost = postService.updateToReported(updateRequest);

        //then
        then(postSearchRepository).should(times(1)).save(captor.capture());
        PostSearchDocument fetchedPostDocumentCaptor = captor.getValue();

        assertThat(fetchedPost.getSalePostId()).isEqualTo(3L);
        assertThat(fetchedPost.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedPost.getTicket()).isEqualTo(ticket);
        assertThat(fetchedPost.getSaleStatus()).isEqualTo(SaleStatus.REPORTED);

        assertThat(fetchedPostDocumentCaptor.getSalePostId()).isEqualTo(3L);
        assertThat(fetchedPostDocumentCaptor.getMemberName()).isEqualTo("seller name");
        assertThat(fetchedPostDocumentCaptor.getSportsName()).isEqualTo("농구");
        assertThat(fetchedPostDocumentCaptor.getStadiumName()).isEqualTo("고척돔");
        assertThat(fetchedPostDocumentCaptor.getHomeTeamName()).isEqualTo("키움");
        assertThat(fetchedPostDocumentCaptor.getAwayTeamName()).isEqualTo("기아");
        assertThat(fetchedPostDocumentCaptor.getQuantity()).isEqualTo(1);
        assertThat(fetchedPostDocumentCaptor.getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPostDocumentCaptor.getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPostDocumentCaptor.getExpirationAt());
        assertThat(fetchedPostDocumentCaptor.getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPostDocumentCaptor.getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPostDocumentCaptor.getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPostDocumentCaptor.getNote()).isEqualTo("note");
        assertThat(fetchedPostDocumentCaptor.getSaleStatus()).isEqualTo(SaleStatus.REPORTED);
        assertNotNull(fetchedPost.getCreatedAt());
    }
}
