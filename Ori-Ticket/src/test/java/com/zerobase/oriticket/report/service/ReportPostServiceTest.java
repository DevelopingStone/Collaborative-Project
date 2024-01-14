package com.zerobase.oriticket.report.service;

import com.zerobase.oriticket.domain.elasticsearch.report.repository.ReportPostSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.dto.RegisterReportPostRequest;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import com.zerobase.oriticket.domain.report.repository.ReportPostRepository;
import com.zerobase.oriticket.domain.report.service.ReportPostService;
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
public class ReportPostServiceTest {

    @Mock
    private ReportPostRepository reportPostRepository;

    @Mock
    private ReportPostSearchRepository reportPostSearchRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportPostService reportPostService;

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

    private ReportPost createReportPost(
            Long reportPostId,
            Member member,
            Post salePost,
            ReportReactStatus status,
            LocalDateTime reactedAt,
            String note
    ){
        return ReportPost.builder()
                .reportPostId(reportPostId)
                .member(member)
                .salePost(salePost)
                .reason(ReportPostType.OTHER_ISSUES)
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
    @Transactional
    @DisplayName("Report Post 등록 성공")
    void successRegister(){
        //given
        RegisterReportPostRequest registerRequest =
                RegisterReportPostRequest.builder()
                        .memberId(2L)
                        .reason("Other_issues")
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "두산");
        Ticket ticket = createTicket(10L, sports, stadium, awayTeam);
        Member member1 = createMember(11L);
        Member member2 = createMember(2L);
        Post salePost = createPost(14L, member1, ticket, SaleStatus.FOR_SALE);
        ReportPost reportPost = createReportPost(5L, member2, salePost,
                ReportReactStatus.PROCESSING, null, null);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member2));
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(salePost));
        given(reportPostRepository.save(any(ReportPost.class)))
                .willReturn(reportPost);

        //when
        ReportPost fetchedReportPost = reportPostService.register(14L, registerRequest);

        //then
        assertThat(fetchedReportPost.getReportPostId()).isEqualTo(5L);
        assertThat(fetchedReportPost.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedReportPost.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedReportPost.getReason()).isEqualTo(ReportPostType.OTHER_ISSUES);
        assertNotNull(fetchedReportPost.getReportedAt());
        assertThat(fetchedReportPost.getStatus()).isEqualTo(ReportReactStatus.PROCESSING);
        assertNull(fetchedReportPost.getReactedAt());
        assertNull(fetchedReportPost.getNote());

    }

    @Test
    @Transactional
    @DisplayName("Report Post 처리 성공")
    void successUpdateToReacted(){
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
        ReportPost reportPost = createReportPost(5L, member2, salePost,
                ReportReactStatus.PROCESSING, LocalDateTime.now(), "react note");

        given(reportPostRepository.findById(anyLong()))
                .willReturn(Optional.of(reportPost));
        given(reportPostRepository.save(any(ReportPost.class)))
                .willReturn(reportPost);

        //when
        ReportPost fetchedReportPost = reportPostService.updateToReacted(14L, updateRequest);

        //then
        assertThat(fetchedReportPost.getReportPostId()).isEqualTo(5L);
        assertThat(fetchedReportPost.getMember().getMemberId()).isEqualTo(2L);
        assertThat(fetchedReportPost.getSalePost()).isEqualTo(salePost);
        assertThat(fetchedReportPost.getReason()).isEqualTo(ReportPostType.OTHER_ISSUES);
        assertNotNull(fetchedReportPost.getReportedAt());
        assertThat(fetchedReportPost.getStatus()).isEqualTo(ReportReactStatus.REACTED);
        assertNotNull(fetchedReportPost.getReactedAt());
        assertThat(fetchedReportPost.getNote()).isEqualTo("react note");

    }
}
