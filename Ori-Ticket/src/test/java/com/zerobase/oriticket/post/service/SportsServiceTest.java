package com.zerobase.oriticket.post.service;

import com.zerobase.oriticket.domain.post.dto.RegisterSportsRequest;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.repository.SportsRepository;
import com.zerobase.oriticket.domain.post.repository.TicketRepository;
import com.zerobase.oriticket.domain.post.service.SportsService;
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
public class SportsServiceTest {

    @Mock
    private SportsRepository sportsRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private SportsService sportsService;

    private Sports createSports(Long sportsId, String sportsName){
        return Sports.builder()
                .sportsId(sportsId)
                .sportsName(sportsName)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Sports 등록 성공")
    void successRegister(){
        //given
        RegisterSportsRequest registerRequest =
                RegisterSportsRequest.builder()
                        .sportsName("야구")
                        .build();
        Sports sports = createSports(1L, "야구");

        given(sportsRepository.save(any(Sports.class)))
                .willReturn(sports);
        //when
        Sports fetchedSports = sportsService.register(registerRequest);

        //then
        assertThat(fetchedSports.getSportsId()).isEqualTo(1L);
        assertThat(fetchedSports.getSportsName()).isEqualTo("야구");
    }

    @Test
    @DisplayName("Sports 조회 성공")
    void successGet(){
        //given
        Sports sports = createSports(2L, "농구");

        given(sportsRepository.findById(anyLong()))
                .willReturn(Optional.of(sports));
        //when
        Sports fetchedSports = sportsService.get(2L);

        //then
        assertThat(fetchedSports.getSportsId()).isEqualTo(2L);
        assertThat(fetchedSports.getSportsName()).isEqualTo("농구");
    }

    @Test
    @DisplayName("모든 Sports 조회 성공")
    void successGetAll(){
        //given
        Sports sports1 = createSports(1L, "야구");
        Sports sports2 = createSports(2L, "농구");
        
        List<Sports> sportsList = Arrays.asList(sports1, sports2);

        given(sportsRepository.findAll())
                .willReturn(sportsList);
        //when
        List<Sports> fetchedSports = sportsService.getAll();

        //then
        assertThat(fetchedSports).hasSize(2);
        assertThat(fetchedSports.get(0).getSportsId()).isEqualTo(1L);
        assertThat(fetchedSports.get(0).getSportsName()).isEqualTo("야구");
        assertThat(fetchedSports.get(1).getSportsId()).isEqualTo(2L);
        assertThat(fetchedSports.get(1).getSportsName()).isEqualTo("농구");
    }

    @Test
    @Transactional
    @DisplayName("Sports 삭제 성공")
    void successDelete(){
        //given
        Sports sports = createSports(1L, "야구");

        sportsRepository.save(sports);

        given(sportsRepository.findById(anyLong()))
                .willReturn(Optional.of(sports));

        ArgumentCaptor<Sports> captor = ArgumentCaptor.forClass(Sports.class);

        //when
        Long fetchedSportsId = sportsService.delete(1L);

        //then
        then(sportsRepository).should(times(1)).save(captor.capture());
        Sports fetchedSports =  captor.getValue();

        assertThat(fetchedSportsId).isEqualTo(1L);
        assertThat(fetchedSports.getSportsId()).isEqualTo(1L);
        assertThat(fetchedSports.getSportsName()).isEqualTo("야구");
    }
}
