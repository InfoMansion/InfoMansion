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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
        given(roomService.findRecommendRoomByUserLikePost()).willReturn(new RoomRecommendResponseDto(roomResponseDtos));
        // when, then
        mockMvc.perform(get("/api/v2/rooms/recommend"))
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

    @Test
    public void Room_이미지_수정() throws Exception {
        // given
        MockMultipartFile roomImg = new MockMultipartFile("roomImg", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
        given(roomService.editRoomImg(any(MockMultipartFile.class))).willReturn(true);
        // when, then
        mockMvc.perform(multipart(HttpMethod.PUT, URI.create("/api/v1/rooms/edit"))
                        .file(roomImg))
                .andExpect(status().isOk())
                .andDo(document("room-edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("roomImg").description("변경할 room 이미지")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("room 이미지 수정 성공")))
                ));
    }

    @Test
    public void Room_이미지_랜덤_조회() throws Exception {
        List<String> response = new ArrayList<>();
        for(int i = 1; i <= 5; i++) response.add("room image url " + i);
        given(roomService.findRandomRoomImage()).willReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/rooms/random"))
                .andExpect(status().isOk())
                .andDo(document("room-random-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("랜덤으로 조회한 Room Image 20개")))
                ));
    }
}
