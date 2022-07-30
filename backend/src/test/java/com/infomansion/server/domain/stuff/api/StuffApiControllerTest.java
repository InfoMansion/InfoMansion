package com.infomansion.server.domain.stuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.global.util.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class StuffApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StuffRepository stuffRepository;

    @AfterEach()
    public void cleanUp() {
        stuffRepository.deleteAll();
    }

    @DisplayName("유효한 StuffType 값이 아니면 Stuff 생성이 불가능합니다.")
    @Test
    public void stuff_생성_실패_1() throws Exception {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT";
        String stuffType = "NONE";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .geometry("geometry")
                .materials("materials")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                post("/api/v1/stuffs")
                        .content(s)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_VALID_METHOD_ARGUMENT.getCode()))
                .andExpect(jsonPath("$.message").value("유효하지 않은 값입니다."));
    }

    @DisplayName("유효한 Category 값이 아니면 Stuff 생성이 불가능합니다.")
    @Test
    public void stuff_생성_실패_2() throws Exception {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,BOOK";
        String stuffType = "OTHER";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .geometry("geometry")
                .materials("materials")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        post("/api/v1/stuffs")
                                .content(s)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_VALID_CATEGORY.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_VALID_CATEGORY.getMessage()));
    }

    @DisplayName("Enum 값이 5개를 초과하면 Stuff 생성이 불가능합니다.")
    @Test
    public void stuff_생성_실패_3() throws Exception {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME,SPORTS,DAILY,ART,MUSIC";
        String stuffType = "OTHER";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .geometry("geometry")
                .materials("materials")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        post("/api/v1/stuffs")
                                .content(s)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.EXCEEDED_THE_NUMBER_OF_CATEGORIES.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.EXCEEDED_THE_NUMBER_OF_CATEGORIES.getMessage()));
    }

}
