package com.zerobase.oriticket.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.post.controller.SportsController;
import com.zerobase.oriticket.domain.post.dto.RegisterSportsRequest;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.service.SportsService;
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

@WebMvcTest(SportsController.class)
public class SportsControllerTest {

    @MockBean
    private SportsService sportsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/sports";

    private final static Long SPORTS_ID = 1L;
    private final static String SPORTS_NAME = "야구";

    private Sports createSports(Long sportsId, String sportsName){
        return Sports.builder()
                .sportsId(sportsId)
                .sportsName(sportsName)
                .build();
    }

    @Test
    @DisplayName("Sports 등록 성공")
    void successRegister() throws Exception{
        //given
        RegisterSportsRequest sportsRequest =
                RegisterSportsRequest.builder()
                        .sportsName(SPORTS_NAME)
                        .build();
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);

        given(sportsService.register(any(RegisterSportsRequest.class)))
                .willReturn(sports);

        //when
        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sportsRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportsId").value(1L))
                .andExpect(jsonPath("$.sportsName").value("야구"));
    }

    @Test
    @DisplayName("Sports 조회 성공")
    void successGet() throws Exception{
        //given
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);

        given(sportsService.get(anyLong()))
                .willReturn(sports);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportsId").value(1L))
                .andExpect(jsonPath("$.sportsName").value("야구"));
    }

    @Test
    @DisplayName("Sports 모두 조회 성공")
    void successGetAll() throws Exception{
        //given
        Sports sports1 = createSports(1L, "야구");
        Sports sports2 = createSports(2L, "축구");
        Sports sports3 = createSports(3L, "농구");
        List<Sports> sportsList 
                = Arrays.asList(sports1, sports2, sports3);

        given(sportsService.getAll())
                .willReturn(sportsList);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].sportsId").value(1L))
                .andExpect(jsonPath("$.[0].sportsName").value("야구"))
                .andExpect(jsonPath("$.[1].sportsId").value(2L))
                .andExpect(jsonPath("$.[1].sportsName").value("축구"))
                .andExpect(jsonPath("$.[2].sportsId").value(3L))
                .andExpect(jsonPath("$.[2].sportsName").value("농구"));
    }

    @Test
    @DisplayName("Sports 삭제 성공")
    void successDelete() throws Exception{
        //given
        given(sportsService.delete(anyLong()))
                .willReturn(SPORTS_ID);

        //when
        //then
        mockMvc.perform(delete(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }
}
