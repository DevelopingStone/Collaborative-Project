package com.zerobase.oriticket.post.service;

import com.zerobase.oriticket.domain.post.dto.RegisterStadiumRequest;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.entity.Stadium;
import com.zerobase.oriticket.domain.post.repository.SportsRepository;
import com.zerobase.oriticket.domain.post.repository.StadiumRepository;
import com.zerobase.oriticket.domain.post.repository.TicketRepository;
import com.zerobase.oriticket.domain.post.service.StadiumService;
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
public class StadiumServiceTest {
    
    @Mock
    private StadiumRepository stadiumRepository;
    
    @Mock
    private SportsRepository sportsRepository;
    
    @Mock
    private TicketRepository ticketRepository;
    
    @InjectMocks
    private StadiumService stadiumService;

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
    
    @Test
    @Transactional
    @DisplayName("Stadium 등록 성공")
    void successRegister(){
        //given
        RegisterStadiumRequest registerRequest =
                RegisterStadiumRequest.builder()
                        .sportsId(1L)
                        .stadiumName("고척돔")
                        .homeTeamName("키움")
                        .build();

        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(2L, sports, "고척돔", "키움");

        given(sportsRepository.findById(anyLong()))
                .willReturn(Optional.of(sports));
        given(stadiumRepository.save(any(Stadium.class)))
                .willReturn(stadium);
        
        //when
        Stadium fetchedStadium = stadiumService.register(registerRequest);
        
        //then
        assertThat(fetchedStadium.getStadiumId()).isEqualTo(2L);
        assertThat(fetchedStadium.getSports()).isEqualTo(sports);
        assertThat(fetchedStadium.getStadiumName()).isEqualTo("고척돔");
        assertThat(fetchedStadium.getHomeTeamName()).isEqualTo("키움");
    }

    @Test
    @DisplayName("Stadium 조회 성공")
    void successGet(){
        //given
        Sports sports = createSports(2L, "농구");
        Stadium stadium = createStadium(1L, sports, "잠실 주경기장", "두산");

        given(stadiumRepository.findById(anyLong()))
                .willReturn(Optional.of(stadium));

        //when
        Stadium fetchedStadium = stadiumService.get(1L);

        //then
        assertThat(fetchedStadium.getStadiumId()).isEqualTo(1L);
        assertThat(fetchedStadium.getSports()).isEqualTo(sports);
        assertThat(fetchedStadium.getStadiumName()).isEqualTo("잠실 주경기장");
        assertThat(fetchedStadium.getHomeTeamName()).isEqualTo("두산");
    }

    @Test
    @DisplayName("모든 Stadium 조회 성공")
    void successGetAll(){
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium1 = createStadium(1L, sports, "잠실 주경기장", "두산");
        Stadium stadium2 = createStadium(2L, sports, "고척돔", "키움");

        List<Stadium> stadiumList = Arrays.asList(stadium1, stadium2);

        given(stadiumRepository.findAll())
                .willReturn(stadiumList);

        //when
        List<Stadium> fetchedStadiums = stadiumService.getAll();

        //then
        assertThat(fetchedStadiums).hasSize(2);
        assertThat(fetchedStadiums.get(0).getStadiumId()).isEqualTo(1L);
        assertThat(fetchedStadiums.get(0).getSports()).isEqualTo(sports);
        assertThat(fetchedStadiums.get(0).getStadiumName()).isEqualTo("잠실 주경기장");
        assertThat(fetchedStadiums.get(0).getHomeTeamName()).isEqualTo("두산");
        assertThat(fetchedStadiums.get(1).getStadiumId()).isEqualTo(2L);
        assertThat(fetchedStadiums.get(1).getSports()).isEqualTo(sports);
        assertThat(fetchedStadiums.get(1).getStadiumName()).isEqualTo("고척돔");
        assertThat(fetchedStadiums.get(1).getHomeTeamName()).isEqualTo("키움");
    }

    @Test
    @DisplayName("SportsId 로 Stadium 조회 성공")
    void successGetBySportsId(){
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium1 = createStadium(1L, sports, "잠실 주경기장", "두산");
        Stadium stadium2 = createStadium(2L, sports, "고척돔", "키움");

        List<Stadium> stadiumList = Arrays.asList(stadium1, stadium2);

        given(stadiumRepository.findAllBySports_SportsId(anyLong()))
                .willReturn(stadiumList);

        //when
        List<Stadium> fetchedStadiums = stadiumService.getBySportsId(1L);

        //then
        assertThat(fetchedStadiums).hasSize(2);
        assertThat(fetchedStadiums.get(0).getStadiumId()).isEqualTo(1L);
        assertThat(fetchedStadiums.get(0).getSports()).isEqualTo(sports);
        assertThat(fetchedStadiums.get(0).getStadiumName()).isEqualTo("잠실 주경기장");
        assertThat(fetchedStadiums.get(0).getHomeTeamName()).isEqualTo("두산");
        assertThat(fetchedStadiums.get(1).getStadiumId()).isEqualTo(2L);
        assertThat(fetchedStadiums.get(1).getSports()).isEqualTo(sports);
        assertThat(fetchedStadiums.get(1).getStadiumName()).isEqualTo("고척돔");
        assertThat(fetchedStadiums.get(1).getHomeTeamName()).isEqualTo("키움");
    }

    @Test
    @Transactional
    @DisplayName("Stadium 삭제 성공")
    void successDelete(){
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "잠실 주경기장", "두산");

        stadiumRepository.save(stadium);

        given(stadiumRepository.findById(anyLong()))
                .willReturn(Optional.of(stadium));

        ArgumentCaptor<Stadium> captor = ArgumentCaptor.forClass(Stadium.class);

        //when
        Long fetchedStadiumId = stadiumService.delete(1L);

        //then
        then(stadiumRepository).should(times(1)).delete(captor.capture());
        Stadium fetchedStadium = captor.getValue();

        assertThat(fetchedStadiumId).isEqualTo(1L);
        assertThat(fetchedStadium.getStadiumId()).isEqualTo(1L);
        assertThat(fetchedStadium.getSports()).isEqualTo(sports);
        assertThat(fetchedStadium.getStadiumName()).isEqualTo("잠실 주경기장");
        assertThat(fetchedStadium.getHomeTeamName()).isEqualTo("두산");
    }
}
