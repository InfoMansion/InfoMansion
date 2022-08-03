package com.infomansion.server.domain.userstuff.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.*;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class UserStuffRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserStuffService userStuffService;

    @Test
    public void userStuff_저장() throws Exception{
        // given
        Long userStuffId = 20L;
        UserStuffRequestDto requestDto = UserStuffRequestDto.builder()
                .userId(10L).stuffId(999L).build();
        given(userStuffService.saveUserStuff(any(UserStuffRequestDto.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(post("/api/v1/userstuffs")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("userstuff-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").description("사용자 Id"),
                                fieldWithPath("stuffId").description("stuff Id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description(USERSTUFF_ID.getDescription())))
                ));
    }

    @Test
    public void userStuffID로_userStuff_조회() throws Exception {
        // given
        Long userStuffId = 20L;
        String categories = "DAILY,STUDY,INTERIOR";
        Stuff stuff = Stuff.builder()
                .id(10L)
                .stuffName("desk")
                .stuffNameKor("책상")
                .price(50L)
                .categories(categories)
                .stuffType(StuffType.DESK)
                .geometry("geometry")
                .material("material")
                .stuffGlbPath("glbPath")
                .build();
        UserStuffResponseDto responseDto = new UserStuffResponseDto(
                    UserStuff.builder()
                        .id(userStuffId)
                        .stuff(stuff)
                        .alias("info팀의 Daily")
                        .category(Category.DAILY)
                        .selected(true)
                        .posX(BigDecimal.ONE).posY(BigDecimal.ONE).posZ(BigDecimal.ONE)
                        .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO).build());
        given(userStuffService.findUserStuffByUserStuffId(any(Long.class))).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/userstuffs/{userstuffId}", userStuffId))
                .andExpect(status().isOk())
                .andDo(document("userstuff-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userstuffId").description("조회할 userStuff Id")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("조회된 UserStuff")))
                                .andWithPrefix("data.", getUserStuffResponse())
                ));
    }

    @Test
    public void userId로_사용자의_모든_userStuff_조회() throws Exception {
        // given
        Long userId = 10L;
        List<UserStuffResponseDto> responseDtoList = new ArrayList<>();
        for(int i = 1; i < 10; i+=2) {
            String categories = "DAILY,STUDY,INTERIOR";
            Stuff stuff = Stuff.builder()
                    .id(10L+i)
                    .stuffName("desk"+i).stuffNameKor("책상"+i)
                    .price(50L+i)
                    .categories(categories)
                    .stuffType(StuffType.DESK)
                    .geometry("geometry")
                    .material("material")
                    .stuffGlbPath("glbPath")
                    .build();
            responseDtoList.add(new UserStuffResponseDto(
                    UserStuff.builder()
                            .id(Long.valueOf(i))
                            .stuff(stuff)
                            .alias("info팀의 Daily")
                            .category(Category.DAILY)
                            .selected(true)
                            .posX(BigDecimal.ONE).posY(BigDecimal.ONE).posZ(BigDecimal.ONE)
                            .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO).build()));
        }
        given(userStuffService.findAllUserStuff(any(Long.class))).willReturn(responseDtoList);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/userstuffs/list/{userId}", userId))
                .andExpect(status().isOk())
                .andDo(document("userstuff-getall-with-userid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("사용자 Id")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용자의 모든 UserStuff")))
                                .andWithPrefix("data.[].", getUserStuffResponse())
                ));
    }

    @Test
    public void 방에_배치된_userStuff_제외() throws Exception {
        // given
        Long userStuffId = 20L;
        given(userStuffService.excludeUserStuff(any(Long.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/userstuffs/{userStuffId}", userStuffId))
                .andExpect(status().isOk())
                .andDo(document("userstuff-exclude-in-room",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userStuffId").description("방에서 제외할 userStuff Id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("제외된 "+USERSTUFF_ID.getDescription())))
                ));
    }

    @Test
    public void userStuff를_방에_배치() throws Exception {
        // given
        Long userStuffId = 20L;
        UserStuffIncludeRequestDto requestDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId)
                .alias("Info`Mansion 이용법 정리")
                .category("DAILY")
                .posX(1.5).posY(3.0).posZ(2.7)
                .rotX(0.0).rotY(0.0).rotZ(0.0).build();
        given(userStuffService.includeUserStuff(any(UserStuffIncludeRequestDto.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("userstuff-include-in-room",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("배치할 userStuff의 Id"),
                                fieldWithPath("alias").description("userStuff의 별칭"),
                                fieldWithPath("category").description("stuff에 설정할 수 있는 카테고리"),
                                fieldWithPath("posX").description("위치좌표 X"),
                                fieldWithPath("posY").description("위치좌표 Y"),
                                fieldWithPath("posZ").description("위치좌표 Z"),
                                fieldWithPath("rotX").description("회전값 X"),
                                fieldWithPath("rotY").description("회전값 Y"),
                                fieldWithPath("rotZ").description("회전값 Z")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("배치된 "+USERSTUFF_ID.getDescription())))
                ));
    }

    @Test
    public void userStuff를_별칭이나_카테고리_수정() throws Exception {
        // given
        Long userStuffId = 20L;
        UserStuffModifyRequestDto requestDto = UserStuffModifyRequestDto.builder()
                        .id(userStuffId).alias("alias").category("NONE").build();
        given(userStuffService.modifyAliasOrCategory(any(UserStuffModifyRequestDto.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs/option")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("userstuff-modify-alias-or-category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("배치할 userStuff의 Id"),
                                fieldWithPath("alias").description("userStuff의 별칭"),
                                fieldWithPath("category").description("stuff에 설정할 수 있는 카테고리")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("수정된 "+USERSTUFF_ID.getDescription())))
                ));
    }

    @Test
    public void userStuff를_위치나_회전_수정() throws Exception {
        // given
        Long userStuffId = 20L;
        UserStuffPositionRequestDto requestDto = UserStuffPositionRequestDto.builder()
                        .id(userStuffId)
                        .posX(0.0).posY(0.0).posZ(0.0)
                        .rotX(0.0).rotY(0.0).rotZ(0.0).build();
        given(userStuffService.modifyPosAndRot(any(UserStuffPositionRequestDto.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs/position")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("userstuff-modify-pos-or-rot",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("배치할 userStuff의 Id"),
                                fieldWithPath("posX").description("위치좌표 X"),
                                fieldWithPath("posY").description("위치좌표 Y"),
                                fieldWithPath("posZ").description("위치좌표 Z"),
                                fieldWithPath("rotX").description("회전값 X"),
                                fieldWithPath("rotY").description("회전값 Y"),
                                fieldWithPath("rotZ").description("회전값 Z")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("수정된 "+USERSTUFF_ID.getDescription())))
                ));
    }

    @Test
    public void userSutff_삭제() throws Exception {
        // given
        Long userStuffId = 20L;
        given(userStuffService.removeUserStuff(any(Long.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/userstuffs/{userStuffId}", userStuffId))
                .andExpect(status().isOk())
                .andDo(document("userstuff-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userStuffId").description("삭제할 userStuff Id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("삭제된 "+USERSTUFF_ID.getJsonFieldType())))
                ));
    }

    private List<FieldDescriptor> getUserStuffResponse() {
        return Arrays.asList(
                fieldWithPath("userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description(USERSTUFF_ID.getDescription()),
                fieldWithPath("stuffName").type(STUFF_NAME.getJsonFieldType()).description(STUFF_NAME.getDescription()),
                fieldWithPath("stuffNameKor").type(STUFF_NAME_KOR.getJsonFieldType()).description(STUFF_NAME_KOR.getDescription()),
                fieldWithPath("stuffType").type(STUFF_TYPE.getJsonFieldType()).description(STUFF_TYPE.getDescription()),
                fieldWithPath("alias").type(ALIAS.getJsonFieldType()).description(ALIAS.getDescription()),
                fieldWithPath("category").type(JsonFieldType.OBJECT).description("Category"),
                fieldWithPath("category.category").type(CATEGORY.getJsonFieldType()).description(CATEGORY.getDescription()),
                fieldWithPath("category.categoryName").type(CATEGORY_NAME.getJsonFieldType()).description(CATEGORY_NAME.getDescription()),
                fieldWithPath("selected").type(USERSTUFF_SELECTED.getJsonFieldType()).description(USERSTUFF_SELECTED.getDescription()),
                fieldWithPath("posX").type(POS_X.getJsonFieldType()).description(POS_X.getDescription()),
                fieldWithPath("posY").type(POS_Y.getJsonFieldType()).description(POS_Y.getDescription()),
                fieldWithPath("posZ").type(POS_Z.getJsonFieldType()).description(POS_Z.getDescription()),
                fieldWithPath("rotX").type(ROT_X.getJsonFieldType()).description(ROT_X.getDescription()),
                fieldWithPath("rotY").type(ROT_Y.getJsonFieldType()).description(ROT_Y.getDescription()),
                fieldWithPath("rotZ").type(ROT_Z.getJsonFieldType()).description(ROT_Z.getDescription()),
                fieldWithPath("createdTime").type(USERSTUFF_SELECTED.getJsonFieldType()).description(USERSTUFF_CREATED.getDescription()).optional(),
                fieldWithPath("modifiedTime").type(USERSTUFF_MODIFIED.getJsonFieldType()).description(USERSTUFF_MODIFIED.getDescription()).optional()
        );
    }
}