package com.zerobase.oriticket.post.controller;

import com.zerobase.oriticket.domain.elasticsearch.post.contorller.PostSearchController;
import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.service.PostSearchService;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostSearchController.class)
public class PostSearchControllerTest {

    @MockBean
    private PostSearchService postSearchService;

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/posts";

    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 15000;
    private final static Boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "A열 4석";
    private final static String IMG_URL = "image url";
    private final static String NOTE = "note";

    private PostSearchDocument createPostDocument(
            Long salePostId,
            String memberName,
            String sportsName,
            String stadiumName,
            String homeTeamName,
            String awayTeamName
    ){
        return PostSearchDocument.builder()
                .salePostId(salePostId)
                .memberName(memberName)
                .sportsName(sportsName)
                .stadiumName(stadiumName)
                .homeTeamName(homeTeamName)
                .awayTeamName(awayTeamName)
                .quantity(QUANTITY)
                .salePrice(SALE_PRICE)
                .originalPrice(ORIGINAL_PRICE)
                .expirationAt(LocalDateTime.now().plusDays(1))
                .isSuccessive(IS_SUCCESSIVE)
                .seatInfo(SEAT_INFO)
                .imgUrl(IMG_URL)
                .note(NOTE)
                .saleStatus(SaleStatus.FOR_SALE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("SalePost 검색 성공")
    void successSearch() throws Exception {
        //given
        PostSearchDocument postSearchDocument1 =
                createPostDocument(1L, "member1", "야구",
                        "잠실 경기장", "두산", "기아");
        PostSearchDocument postSearchDocument2 =
                createPostDocument(2L, "member2", "야구",
                        "잠실 경기장", "두산", "한화");
        List<PostSearchDocument> postSearchDocumentList =
                Arrays.asList(postSearchDocument1, postSearchDocument2);
        Page<PostSearchDocument> postSearchDocuments =
                new PageImpl<>(postSearchDocumentList);

        given(postSearchService.search(anyString(), anyInt(), anyInt()))
                .willReturn(postSearchDocuments);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/search?value="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].salePostId").value(1L))
                .andExpect(jsonPath("$.content[0].memberName").value("member1"))
                .andExpect(jsonPath("$.content[0].sportsName").value("야구"))
                .andExpect(jsonPath("$.content[0].stadiumName").value("잠실 경기장"))
                .andExpect(jsonPath("$.content[0].homeTeamName").value("두산"))
                .andExpect(jsonPath("$.content[0].awayTeamName").value("기아"))
                .andExpect(jsonPath("$.content[0].quantity").value(1))
                .andExpect(jsonPath("$.content[0].salePrice").value(10000))
                .andExpect(jsonPath("$.content[0].originalPrice").value(15000))
                .andExpect(jsonPath("$.content[0].expirationAt").exists())
                .andExpect(jsonPath("$.content[0].isSuccessive").value(false))
                .andExpect(jsonPath("$.content[0].seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.content[0].imgUrl").value("image url"))
                .andExpect(jsonPath("$.content[0].note").value("note"))
                .andExpect(jsonPath("$.content[0].saleStatus").value("FOR_SALE"))
                .andExpect(jsonPath("$.content[0].createdAt").exists())
                .andExpect(jsonPath("$.content[1].salePostId").value(2L))
                .andExpect(jsonPath("$.content[1].memberName").value("member2"))
                .andExpect(jsonPath("$.content[1].sportsName").value("야구"))
                .andExpect(jsonPath("$.content[1].stadiumName").value("잠실 경기장"))
                .andExpect(jsonPath("$.content[1].homeTeamName").value("두산"))
                .andExpect(jsonPath("$.content[1].awayTeamName").value("한화"))
                .andExpect(jsonPath("$.content[1].quantity").value(1))
                .andExpect(jsonPath("$.content[1].salePrice").value(10000))
                .andExpect(jsonPath("$.content[1].originalPrice").value(15000))
                .andExpect(jsonPath("$.content[1].expirationAt").exists())
                .andExpect(jsonPath("$.content[1].isSuccessive").value(false))
                .andExpect(jsonPath("$.content[1].seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.content[1].imgUrl").value("image url"))
                .andExpect(jsonPath("$.content[1].note").value("note"))
                .andExpect(jsonPath("$.content[1].saleStatus").value("FOR_SALE"))
                .andExpect(jsonPath("$.content[1].createdAt").exists());

    }

}
