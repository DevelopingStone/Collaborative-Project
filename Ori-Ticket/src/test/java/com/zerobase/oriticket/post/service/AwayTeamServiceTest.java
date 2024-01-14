package com.zerobase.oriticket.post.service;

import com.zerobase.oriticket.domain.post.dto.RegisterAwayTeamRequest;
import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.repository.AwayTeamRepository;
import com.zerobase.oriticket.domain.post.repository.SportsRepository;
import com.zerobase.oriticket.domain.post.repository.TicketRepository;
import com.zerobase.oriticket.domain.post.service.AwayTeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AwayTeamServiceTest {

    @Mock
    private AwayTeamRepository awayTeamRepository;

    @Mock
    private SportsRepository sportsRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private AwayTeamService awayTeamService;

    private Sports createSports(Long sportsId){
        return Sports.builder()
                .sportsId(sportsId)
                .build();
    }

    private AwayTeam createAwayTeam(Long awayTeamId, Long sportsId, String awayTeamName){
        return AwayTeam.builder()
                .awayTeamId(awayTeamId)
                .sports(createSports(sportsId))
                .awayTeamName(awayTeamName)
                .build();
    }

    private AwayTeam createAwayTeam(Long awayTeamId, Sports sports, String awayTeamName){
        return AwayTeam.builder()
                .awayTeamId(awayTeamId)
                .sports(sports)
                .awayTeamName(awayTeamName)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("AwayTeam 등록 성공")
    void successRegister(){
        //given
        RegisterAwayTeamRequest registerRequest =
                RegisterAwayTeamRequest.builder()
                        .sportsId(1L)
                        .awayTeamName("기아")
                        .build();

        Sports sports = createSports(1L);

        given(sportsRepository.findById(anyLong()))
                .willReturn(Optional.of(sports));
        given(awayTeamRepository.save(any(AwayTeam.class)))
                .willReturn(createAwayTeam(1L, sports, "기아"));

        //when
        AwayTeam fetchedAwayTeam = awayTeamService.register(registerRequest);

        //then
        assertThat(fetchedAwayTeam.getAwayTeamId()).isEqualTo(1L);
        assertThat(fetchedAwayTeam.getSports()).isEqualTo(sports);
        assertThat(fetchedAwayTeam.getAwayTeamName()).isEqualTo("기아");
    }

    @Test
    @DisplayName("AwayTeam 조회 성공")
    void successGet(){
        //given
        AwayTeam awayTeam = createAwayTeam(1L, 1L, "기아");

        given(awayTeamRepository.findById(anyLong()))
                .willReturn(Optional.of(awayTeam));

        //when
        AwayTeam fetchedAwayTeam = awayTeamService.get(1L);

        //then
        assertThat(fetchedAwayTeam.getAwayTeamId()).isEqualTo(1L);
        assertThat(fetchedAwayTeam.getSports()).isEqualTo(awayTeam.getSports());
        assertThat(fetchedAwayTeam.getAwayTeamName()).isEqualTo("기아");
    }

    @Test
    @DisplayName("모든 AwayTeam 조회 성공")
    void successGetAll(){
        //given
        AwayTeam awayTeam1 = createAwayTeam(1L, 1L, "기아");
        AwayTeam awayTeam2 = createAwayTeam(2L, 1L, "한화");
        List<AwayTeam> awayTeamList = Arrays.asList(awayTeam1, awayTeam2);

        given(awayTeamRepository.findAll())
                .willReturn(awayTeamList);

        //when
        List<AwayTeam> fetchedAwayTeams = awayTeamService.getAll();

        //then
        assertThat(fetchedAwayTeams).hasSize(2);
        assertThat(fetchedAwayTeams.get(0).getAwayTeamId()).isEqualTo(1L);
        assertThat(fetchedAwayTeams.get(0).getSports()).isEqualTo(awayTeam1.getSports());
        assertThat(fetchedAwayTeams.get(0).getAwayTeamName()).isEqualTo("기아");
        assertThat(fetchedAwayTeams.get(1).getAwayTeamId()).isEqualTo(2L);
        assertThat(fetchedAwayTeams.get(1).getSports()).isEqualTo(awayTeam2.getSports());
        assertThat(fetchedAwayTeams.get(1).getAwayTeamName()).isEqualTo("한화");
    }

    @Test
    @DisplayName("SportsId 로 AwayTeam 조회 성공")
    void successGetBySportsId(){
        //given
        AwayTeam awayTeam1 = createAwayTeam(1L, 1L, "기아");
        AwayTeam awayTeam2 = createAwayTeam(2L, 1L, "한화");

        List<AwayTeam> awayTeamList = Arrays.asList(awayTeam1, awayTeam2);

        given(awayTeamRepository.findAllBySports_SportsId(anyLong()))
                .willReturn(awayTeamList);

        //when
        List<AwayTeam> fetchedAwayTeams = awayTeamService.getBySportsId(1L);

        //then
        assertThat(fetchedAwayTeams).hasSize(2);
        assertThat(fetchedAwayTeams.get(0).getAwayTeamId()).isEqualTo(1L);
        assertThat(fetchedAwayTeams.get(0).getSports()).isEqualTo(awayTeam1.getSports());
        assertThat(fetchedAwayTeams.get(0).getAwayTeamName()).isEqualTo("기아");
        assertThat(fetchedAwayTeams.get(1).getAwayTeamId()).isEqualTo(2L);
        assertThat(fetchedAwayTeams.get(1).getSports()).isEqualTo(awayTeam2.getSports());
        assertThat(fetchedAwayTeams.get(1).getAwayTeamName()).isEqualTo("한화");
    }

    @Test
    @Transactional
    @DisplayName("AwayTeam 삭제 성공")
    void successDelete(){
        //given
        AwayTeam awayTeam = createAwayTeam(1L, 1L, "기아");

        given(awayTeamRepository.findById(anyLong()))
                .willReturn(Optional.of(awayTeam));

        awayTeamRepository.save(awayTeam);

        ArgumentCaptor<AwayTeam> captor = ArgumentCaptor.forClass(AwayTeam.class);

        //when
        Long fetchedAwayTeamId = awayTeamService.delete(1L);

        //then
        then(awayTeamRepository).should(times(1)).delete(captor.capture());
        AwayTeam fetchedAwayTeam = captor.getValue();

        assertThat(fetchedAwayTeam.getAwayTeamId()).isEqualTo(1L);
        assertThat(fetchedAwayTeam.getSports()).isEqualTo(awayTeam.getSports());
        assertThat(fetchedAwayTeam.getAwayTeamName()).isEqualTo("기아");
        assertThat(fetchedAwayTeamId).isEqualTo(1L);
    }
}
