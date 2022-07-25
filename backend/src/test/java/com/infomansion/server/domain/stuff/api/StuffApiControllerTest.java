package com.infomansion.server.domain.stuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.service.StuffService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StuffApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StuffApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StuffService stuffService;

    @DisplayName("유효한 Enum 값이 아니면 Stuff 생성이 불가능합니다.")
    @WithMockUser(roles = "USER")
    @Test
    public void stuff_생성_실패() throws Exception {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,BOOK";
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
                .andExpect(jsonPath("$.success").value("false"));
    }

}
