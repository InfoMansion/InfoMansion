package com.infomansion.server.domain.room.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.room.domain.Room;
import com.infomansion.server.domain.room.dto.RoomRecommendResponseDto;
import com.infomansion.server.domain.room.dto.RoomResponseDto;
import com.infomansion.server.domain.room.service.RoomService;
import com.infomansion.server.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class RoomRestDocs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService roomService;

    @Test
    public void Room_추천_검색() throws Exception {
        // given
        List<RoomResponseDto> roomResponseDtos = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            roomResponseDtos.add(new RoomResponseDto(Room.builder()
                    .user(User.builder().username("test"+i).build()).build()));
        }
        given(roomService.findRecommendRoom()).willReturn(new RoomRecommendResponseDto(roomResponseDtos));
        // when, then
        mockMvc.perform(get("/api/v1/rooms/recommend"))
                .andExpect(status().isOk())
                .andDo(document("room-recommend",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.VARIES).description("사용자 이름으로 검색한 결과")))
                                .andWithPrefix("data.",
                                        fieldWithPath("roomResponseDtos[]").type(JsonFieldType.ARRAY).description("추천된 방의 정보"),
                                        fieldWithPath("roomResponseDtos.[].userName").type(USERNAME.getJsonFieldType()).description(USERNAME.getDescription()),
                                        fieldWithPath("roomResponseDtos.[].roomImg").type(ROOM_IMG.getJsonFieldType()).description(ROOM_IMG.getDescription())
                                )

                ));
    }
}
