package com.zerobase.oriticket.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.post.controller.AwayTeamController;
import com.zerobase.oriticket.domain.post.dto.RegisterAwayTeamRequest;
import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.service.AwayTeamService;
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

@WebMvcTest(AwayTeamController.class)
public class    AwayTeamControllerTest {

    @MockBean
    private AwayTeamService awayTeamService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/awayteam";

    private final static Long AWAY_TEAM_ID = 1L;
    private final static Long SPORTS_ID = 1L;
    private final static String SPORTS_NAME = "야구";
    private final static String AWAY_TEAM_NAME = "두산";

    private Sports createSports(Long sportsId, String sportsName) {
        return Sports.builder()
                .sportsId(sportsId)
                .sportsName(sportsName)
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
    @DisplayName("AwayTeam 등록 성공")
    void successRegister() throws Exception{
        //given
        RegisterAwayTeamRequest awayTeamRequest =
                RegisterAwayTeamRequest.builder()
                        .sportsId(SPORTS_ID)
                        .awayTeamName(AWAY_TEAM_NAME)
                        .build();

        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);

        given(awayTeamService.register(any(RegisterAwayTeamRequest.class)))
                .willReturn(AwayTeam.builder()
                        .awayTeamId(AWAY_TEAM_ID)
                        .sports(sports)
                        .awayTeamName(AWAY_TEAM_NAME)
                        .build());

        //when
        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(awayTeamRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.awayTeamId").value(1L))
                .andExpect(jsonPath("$.sportsId").value(1L))
                .andExpect(jsonPath("$.awayTeamName").value("두산"));
    }

    @Test
    @DisplayName("AwayTeam 조회 성공")
    void successGet() throws Exception{
        //given
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);

        given(awayTeamService.get(anyLong()))
                .willReturn(AwayTeam.builder()
                        .awayTeamId(AWAY_TEAM_ID)
                        .sports(sports)
                        .awayTeamName(AWAY_TEAM_NAME)
                        .build());

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.awayTeamId").value(1L))
                .andExpect(jsonPath("$.sportsId").value(1L))
                .andExpect(jsonPath("$.awayTeamName").value("두산"));
    }

    @Test
    @DisplayName("AwayTeam 모두 조회 성공")
    void successGetAll() throws Exception{
        //given
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);

        AwayTeam awayTeam1 = createAwayTeam(1L, sports, "기아");
        AwayTeam awayTeam2 = createAwayTeam(2L, sports, "한화");
        AwayTeam awayTeam3 = createAwayTeam(3L, sports, "두산");

        List<AwayTeam> awayTeamList
                = Arrays.asList(awayTeam1, awayTeam2, awayTeam3);

        given(awayTeamService.getAll())
                .willReturn(awayTeamList);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].awayTeamId").value(1L))
                .andExpect(jsonPath("$.[0].sportsId").value(1L))
                .andExpect(jsonPath("$.[0].awayTeamName").value("기아"))
                .andExpect(jsonPath("$.[1].awayTeamId").value(2L))
                .andExpect(jsonPath("$.[1].sportsId").value(1L))
                .andExpect(jsonPath("$.[1].awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[2].awayTeamId").value(3L))
                .andExpect(jsonPath("$.[2].sportsId").value(1L))
                .andExpect(jsonPath("$.[2].awayTeamName").value("두산"));

    }

    @Test
    @DisplayName("SportId로 AwayTeam 조회 성공")
    void successGetBySportId() throws Exception{
        //given
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);

        AwayTeam awayTeam = createAwayTeam(AWAY_TEAM_ID, sports, AWAY_TEAM_NAME);

        List<AwayTeam> awayTeamList
                = Arrays.asList(awayTeam);

        given(awayTeamService.getBySportsId(SPORTS_ID))
                .willReturn(awayTeamList);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/sports?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].awayTeamId").value(1L))
                .andExpect(jsonPath("$.[0].sportsId").value(1L))
                .andExpect(jsonPath("$.[0].awayTeamName").value("두산"));

    }

    @Test
    @DisplayName("AwayTeam 삭제 성공")
    void successDelete() throws Exception{
        //given
        given(awayTeamService.delete(anyLong()))
                .willReturn(AWAY_TEAM_ID);

        //when
        //then
        mockMvc.perform(delete(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }
}
