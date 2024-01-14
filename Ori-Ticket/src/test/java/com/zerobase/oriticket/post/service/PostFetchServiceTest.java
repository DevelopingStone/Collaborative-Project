package com.zerobase.oriticket.post.service;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.post.service.PostFetchService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostFetchServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostFetchService postFetchService;

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

    private Post createPost(Long salePostId, Member member, Ticket ticket, SaleStatus status){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(status)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("특정 멤버의 판매중인 SalePost 조회 성공")
    void successGetSale(){
        //given
        List<SaleStatus> saleStatusList = Arrays.asList(SaleStatus.FOR_SALE, SaleStatus.TRADING);
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket1 = createTicket(1L, sports, stadium, awayTeam);
        Ticket ticket2 = createTicket(2L, sports, stadium, awayTeam);
        Member member = createMember(1L);
        Post post1 = createPost(1L, member, ticket1, SaleStatus.FOR_SALE);
        Post post2 = createPost(2L, member, ticket2, SaleStatus.TRADING);
        List<Post> postList = Arrays.asList(post1, post2);

        given(postRepository.findAllByMember_MemberIdAndSaleStatusIn(anyLong(), anyList()))
                .willReturn(postList);

        //when
        List<Post> fetchedPost = postFetchService.get(1L, saleStatusList);

        //then
        assertThat(fetchedPost.get(0).getSalePostId()).isEqualTo(1L);
        assertThat(fetchedPost.get(0).getMember().getMemberId()).isEqualTo(1L);
        assertThat(fetchedPost.get(0).getTicket().getTicketId()).isEqualTo(1L);
        assertThat(fetchedPost.get(0).getTicket().getSports()).isEqualTo(sports);
        assertThat(fetchedPost.get(0).getTicket().getStadium()).isEqualTo(stadium);
        assertThat(fetchedPost.get(0).getTicket().getAwayTeam()).isEqualTo(awayTeam);
        assertThat(fetchedPost.get(0).getTicket().getQuantity()).isEqualTo(1);
        assertThat(fetchedPost.get(0).getTicket().getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPost.get(0).getTicket().getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPost.get(0).getTicket().getExpirationAt());
        assertThat(fetchedPost.get(0).getTicket().getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPost.get(0).getTicket().getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPost.get(0).getTicket().getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPost.get(0).getTicket().getNote()).isEqualTo("note");
        assertThat(fetchedPost.get(0).getSaleStatus()).isEqualTo(SaleStatus.FOR_SALE);
        assertNotNull(fetchedPost.get(0).getCreatedAt());
        assertThat(fetchedPost.get(1).getSalePostId()).isEqualTo(2L);
        assertThat(fetchedPost.get(1).getMember().getMemberId()).isEqualTo(1L);
        assertThat(fetchedPost.get(1).getTicket().getTicketId()).isEqualTo(2L);
        assertThat(fetchedPost.get(1).getTicket().getSports()).isEqualTo(sports);
        assertThat(fetchedPost.get(1).getTicket().getStadium()).isEqualTo(stadium);
        assertThat(fetchedPost.get(1).getTicket().getAwayTeam()).isEqualTo(awayTeam);
        assertThat(fetchedPost.get(1).getTicket().getQuantity()).isEqualTo(1);
        assertThat(fetchedPost.get(1).getTicket().getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPost.get(1).getTicket().getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPost.get(1).getTicket().getExpirationAt());
        assertThat(fetchedPost.get(1).getTicket().getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPost.get(1).getTicket().getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPost.get(1).getTicket().getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPost.get(1).getTicket().getNote()).isEqualTo("note");
        assertThat(fetchedPost.get(1).getSaleStatus()).isEqualTo(SaleStatus.TRADING);
        assertNotNull(fetchedPost.get(1).getCreatedAt());
    }

    @Test
    @DisplayName("특정 멤버의 판매종료된 SalePost 조회 성공")
    void successGetEnd(){
        //given
        List<SaleStatus> saleStatusList = Arrays.asList(SaleStatus.SOLD, SaleStatus.REPORTED);
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket1 = createTicket(1L, sports, stadium, awayTeam);
        Ticket ticket2 = createTicket(2L, sports, stadium, awayTeam);
        Member member = createMember(1L);
        Post post1 = createPost(1L, member, ticket1, SaleStatus.SOLD);
        Post post2 = createPost(2L, member, ticket2, SaleStatus.REPORTED);
        List<Post> postList = Arrays.asList(post1, post2);

        given(postRepository.findAllByMember_MemberIdAndSaleStatusIn(anyLong(), anyList()))
                .willReturn(postList);

        //when
        List<Post> fetchedPost = postFetchService.get(1L, saleStatusList);

        //then
        assertThat(fetchedPost.get(0).getSalePostId()).isEqualTo(1L);
        assertThat(fetchedPost.get(0).getMember().getMemberId()).isEqualTo(1L);
        assertThat(fetchedPost.get(0).getTicket().getTicketId()).isEqualTo(1L);
        assertThat(fetchedPost.get(0).getTicket().getSports()).isEqualTo(sports);
        assertThat(fetchedPost.get(0).getTicket().getStadium()).isEqualTo(stadium);
        assertThat(fetchedPost.get(0).getTicket().getAwayTeam()).isEqualTo(awayTeam);
        assertThat(fetchedPost.get(0).getTicket().getQuantity()).isEqualTo(1);
        assertThat(fetchedPost.get(0).getTicket().getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPost.get(0).getTicket().getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPost.get(0).getTicket().getExpirationAt());
        assertThat(fetchedPost.get(0).getTicket().getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPost.get(0).getTicket().getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPost.get(0).getTicket().getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPost.get(0).getTicket().getNote()).isEqualTo("note");
        assertThat(fetchedPost.get(0).getSaleStatus()).isEqualTo(SaleStatus.SOLD);
        assertNotNull(fetchedPost.get(0).getCreatedAt());
        assertThat(fetchedPost.get(1).getSalePostId()).isEqualTo(2L);
        assertThat(fetchedPost.get(1).getMember().getMemberId()).isEqualTo(1L);
        assertThat(fetchedPost.get(1).getTicket().getTicketId()).isEqualTo(2L);
        assertThat(fetchedPost.get(1).getTicket().getSports()).isEqualTo(sports);
        assertThat(fetchedPost.get(1).getTicket().getStadium()).isEqualTo(stadium);
        assertThat(fetchedPost.get(1).getTicket().getAwayTeam()).isEqualTo(awayTeam);
        assertThat(fetchedPost.get(1).getTicket().getQuantity()).isEqualTo(1);
        assertThat(fetchedPost.get(1).getTicket().getSalePrice()).isEqualTo(10000);
        assertThat(fetchedPost.get(1).getTicket().getOriginalPrice()).isEqualTo(15000);
        assertNotNull(fetchedPost.get(1).getTicket().getExpirationAt());
        assertThat(fetchedPost.get(1).getTicket().getIsSuccessive()).isEqualTo(false);
        assertThat(fetchedPost.get(1).getTicket().getSeatInfo()).isEqualTo("A열 4석");
        assertThat(fetchedPost.get(1).getTicket().getImgUrl()).isEqualTo("image url");
        assertThat(fetchedPost.get(1).getTicket().getNote()).isEqualTo("note");
        assertThat(fetchedPost.get(1).getSaleStatus()).isEqualTo(SaleStatus.REPORTED);
        assertNotNull(fetchedPost.get(1).getCreatedAt());
    }
}
