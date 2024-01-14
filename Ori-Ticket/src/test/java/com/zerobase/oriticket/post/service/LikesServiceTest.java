package com.zerobase.oriticket.post.service;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.dto.RegisterLikesRequest;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.LikesRepository;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.post.service.LikesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LikesServiceTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private LikesService likesService;

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

    private Post createPost(Long salePostId, Ticket ticket){
        return Post.builder()
                .salePostId(salePostId)
                .ticket(ticket)
                .build();
    }

    private Likes createLikes(Long likesId, Member member, Post salePost){
        return Likes.builder()
                .likesId(likesId)
                .member(member)
                .salePost(salePost)
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("찜하기에 등록 완료")
    void successRegister(){
        //given
        RegisterLikesRequest registerRequest = RegisterLikesRequest.builder()
                .memberId(1L)
                .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(12L, sports, stadium, awayTeam);
        Post salePost = createPost(50L, ticket);
        Member member = createMember(10L);
        Likes likes = createLikes(1L, member, salePost);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(salePost));
        given(likesRepository.existsByMember_MemberIdAndSalePost(anyLong(), any(Post.class)))
                .willReturn(false);
        given(likesRepository.save(any(Likes.class)))
                .willReturn(likes);

        //when
        Likes fetchedLikes = likesService.register(50L, registerRequest);

        //then
        assertThat(fetchedLikes.getLikesId()).isEqualTo(1L);
        assertThat(fetchedLikes.getMember().getMemberId()).isEqualTo(10L);
        assertThat(fetchedLikes.getSalePost()).isEqualTo(salePost);
    }

    @Test
    @DisplayName("찜하기에서 삭제 완료")
    void successDelete(){
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(12L, sports, stadium, awayTeam);
        Post salePost = createPost(50L, ticket);
        Member member = createMember(10L);
        Likes likes = createLikes(1L, member, salePost);

        given(likesRepository.findBySalePost_SalePostIdAndMember_MemberId(anyLong(), anyLong()))
                .willReturn(Optional.of(likes));

        //when
        Long fetchedLikesId = likesService.delete(1L, 10L);

        //then
        assertThat(fetchedLikesId).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 멤버의 모든 찜하기 조회 완료")
    void successGet(){
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket1 = createTicket(12L, sports, stadium, awayTeam);
        Ticket ticket2 = createTicket(13L, sports, stadium, awayTeam);
        Post salePost1 = createPost(50L, ticket1);
        Post salePost2 = createPost(51L, ticket2);
        Member member = createMember(10L);
        Likes likes1 = createLikes(1L, member, salePost1);
        Likes likes2 = createLikes(2L, member, salePost2);
        List<Likes> likesList = Arrays.asList(likes1, likes2);

        given(likesRepository.findAllByMember_MemberIdAndSalePost_SaleStatusNotIn(anyLong(), anyList()))
                .willReturn(likesList);

        //when
        List<Likes> fetchedLikes = likesService.get(10L);

        //then
        assertThat(fetchedLikes.get(0).getLikesId()).isEqualTo(1L);
        assertThat(fetchedLikes.get(0).getMember().getMemberId()).isEqualTo(10L);
        assertThat(fetchedLikes.get(0).getSalePost()).isEqualTo(salePost1);
        assertThat(fetchedLikes.get(1).getLikesId()).isEqualTo(2L);
        assertThat(fetchedLikes.get(1).getMember().getMemberId()).isEqualTo(10L);
        assertThat(fetchedLikes.get(1).getSalePost()).isEqualTo(salePost2);
    }
}
