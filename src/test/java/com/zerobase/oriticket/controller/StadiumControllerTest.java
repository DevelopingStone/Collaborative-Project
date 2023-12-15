package com.zerobase.oriticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.post.controller.StadiumController;
import com.zerobase.oriticket.domain.post.dto.RegisterStadiumRequest;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.entity.Stadium;
import com.zerobase.oriticket.domain.post.service.StadiumService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StadiumController.class)
public class StadiumControllerTest {

    @MockBean
    private StadiumService stadiumService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/stadium";

    private final static Long STADIUM_ID = 1L;
    private final static Long SPORTS_ID = 1L;
    private final static String SPORTS_NAME = "야구";
    private final static String STADIUM_NAME = "고척 돔";
    private final static String HOME_TEAM_NAME = "한화";

    @Test
    @DisplayName("Stadium 등록 성공")
    void successRegister() throws Exception{
        //given
        RegisterStadiumRequest stadiumRequest =
                RegisterStadiumRequest.builder()
                        .sportsId(SPORTS_ID)
                        .stadiumName(STADIUM_NAME)
                        .homeTeamName(HOME_TEAM_NAME)
                        .build();
        
        Sports sports = Sports.builder()
                .sportsId(SPORTS_ID)
                .sportsName(SPORTS_NAME)
                .build();
        
        given(stadiumService.register(any()))
                .willReturn(Stadium.builder()
                        .stadiumId(STADIUM_ID)
                        .sports(sports)
                        .stadiumName(STADIUM_NAME)
                        .homeTeamName(HOME_TEAM_NAME)
                        .build());

        //when
        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stadiumRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stadiumId").value(1L))
                .andExpect(jsonPath("$.sportsId").value(1L))
                .andExpect(jsonPath("$.stadiumName").value("고척 돔"))
                .andExpect(jsonPath("$.homeTeamName").value("한화"));
    }

    @Test
    @DisplayName("Stadium 조회 성공")
    void successGet() throws Exception{
        //given
        Sports sports = Sports.builder()
                .sportsId(SPORTS_ID)
                .sportsName(SPORTS_NAME)
                .build();

        given(stadiumService.get(anyLong()))
                .willReturn(Stadium.builder()
                        .stadiumId(STADIUM_ID)
                        .sports(sports)
                        .stadiumName(STADIUM_NAME)
                        .homeTeamName(HOME_TEAM_NAME)
                        .build());

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stadiumId").value(1L))
                .andExpect(jsonPath("$.sportsId").value(1L))
                .andExpect(jsonPath("$.stadiumName").value("고척 돔"))
                .andExpect(jsonPath("$.homeTeamName").value("한화"));
    }

    @Test
    @DisplayName("Stadium 모두 조회 성공")
    void successGetAll() throws Exception{
        //given
        Sports sports = Sports.builder()
                .sportsId(SPORTS_ID)
                .sportsName(SPORTS_NAME)
                .build();

        Stadium stadium1 = Stadium.builder()
                .stadiumId(1L)
                .sports(sports)
                .stadiumName("고척 돔")
                .homeTeamName("한화")
                .build();

        Stadium stadium2 = Stadium.builder()
                .stadiumId(2L)
                .sports(sports)
                .stadiumName("잠실 야구장")
                .homeTeamName("두산")
                .build();

        Stadium stadium3 = Stadium.builder()
                .stadiumId(3L)
                .sports(sports)
                .stadiumName("챔피언스 필드")
                .homeTeamName("기아")
                .build();


        List<Stadium> stadiumList
                = Arrays.asList(stadium1, stadium2, stadium3);

        given(stadiumService.getAll())
                .willReturn(stadiumList);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].stadiumId").value(1L))
                .andExpect(jsonPath("$.[0].sportsId").value(1L))
                .andExpect(jsonPath("$.[0].stadiumName").value("고척 돔"))
                .andExpect(jsonPath("$.[0].homeTeamName").value("한화"))
                .andExpect(jsonPath("$.[1].stadiumId").value(2L))
                .andExpect(jsonPath("$.[1].sportsId").value(1L))
                .andExpect(jsonPath("$.[1].stadiumName").value("잠실 야구장"))
                .andExpect(jsonPath("$.[1].homeTeamName").value("두산"))
                .andExpect(jsonPath("$.[2].stadiumId").value(3L))
                .andExpect(jsonPath("$.[2].sportsId").value(1L))
                .andExpect(jsonPath("$.[2].stadiumName").value("챔피언스 필드"))
                .andExpect(jsonPath("$.[2].homeTeamName").value("기아"));

    }

    @Test
    @DisplayName("SportId로 Stadium 조회 성공")
    void successGetBySportId() throws Exception{
        //given
        Sports sports = Sports.builder()
                .sportsId(SPORTS_ID)
                .sportsName(SPORTS_NAME)
                .build();

        Stadium stadium = Stadium.builder()
                .stadiumId(STADIUM_ID)
                .sports(sports)
                .stadiumName(STADIUM_NAME)
                .homeTeamName(HOME_TEAM_NAME)
                .build();

        List<Stadium> stadiumList
                = Arrays.asList(stadium);

        given(stadiumService.getBySportsId(SPORTS_ID))
                .willReturn(stadiumList);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/sports?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].stadiumId").value(1L))
                .andExpect(jsonPath("$.[0].sportsId").value(1L))
                .andExpect(jsonPath("$.[0].stadiumName").value("고척 돔"))
                .andExpect(jsonPath("$.[0].homeTeamName").value("한화"));

    }

    @Test
    @DisplayName("Stadium 삭제 성공")
    void successDelete() throws Exception{
        //given
        given(stadiumService.delete(anyLong()))
                .willReturn(STADIUM_ID);

        //when
        //then
        mockMvc.perform(delete(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }
}
