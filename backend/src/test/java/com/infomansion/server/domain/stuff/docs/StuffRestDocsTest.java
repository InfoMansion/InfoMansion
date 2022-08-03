package com.infomansion.server.domain.stuff.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.stuff.service.StuffService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class StuffRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StuffService stuffService;

    @Test
    public void stuffId로_stuff조회() throws Exception {
        // given
        String categories = "DAILY,STUDY,INTERIOR";
        Stuff stuff = Stuff.builder()
                .id(1L)
                .stuffName("desk")
                .stuffNameKor("책상")
                .price(50L)
                .categories(categories)
                .stuffType(StuffType.DESK)
                .geometry("geometry")
                .material("material")
                .stuffGlbPath("glbPath")
                .build();
        StuffResponseDto responseDto = new StuffResponseDto(stuff);
        given(stuffService.findStuffById(1L)).willReturn(responseDto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/stuffs/{stuffId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("stuff-get-by-stuffid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stuffId").description("조회할 stuff의 id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("조회된 Stuff")))
                                .andWithPrefix("data.", getStuffResponse())
                ));
    }

    @Test
    public void 모든_stuff_조회() throws Exception {
        // given
        List<StuffResponseDto> responseDtoList = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            String categories = "DAILY,STUDY,INTERIOR";
            Stuff stuff = Stuff.builder()
                    .id(1L)
                    .stuffName("desk"+i)
                    .stuffNameKor("책상"+i)
                    .price(50L)
                    .categories(categories)
                    .stuffType(StuffType.DESK)
                    .geometry("geometry")
                    .material("material")
                    .stuffGlbPath("glbPath")
                    .build();
            responseDtoList.add(new StuffResponseDto(stuff));
        }
        given(stuffService.findAllStuff()).willReturn(responseDtoList);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stuffs"))
                .andExpect(status().isOk())
                .andDo(document("stuff-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("모든 Stuff")))
                                .andWithPrefix("data.[].", getStuffResponse())
                ));
    }

    @Test
    public void 특정stuff_수정() throws Exception {
        // given
        Long responseId = 1L;
        String categories = "DAILY,STUDY,INTERIOR";
        StuffRequestDto stuffRequestDto = StuffRequestDto.builder()
                .stuffName("desk")
                .stuffNameKor("책상")
                .price(50L)
                .categories(categories)
                .stuffType("DESK")
                .geometry("geometry")
                .material("material")
                .build();
        given(stuffService.updateStuff(responseId, stuffRequestDto)).willReturn(responseId);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/stuffs/{stuffId}", responseId)
                    .content(objectMapper.writeValueAsString(stuffRequestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stuff-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stuffId").description("수정할 stuff의 id")
                        ),
                        requestFields(
                                fieldWithPath("stuffName").description("stuff 영문명"),
                                fieldWithPath("stuffNameKor").description("stuff 한글명"),
                                fieldWithPath("price").description("stuff 가격"),
                                fieldWithPath("categories").description("stuff에 적용할 수 있는 카테고리"),
                                fieldWithPath("stuffType").description("stuff 가구 타입"),
                                fieldWithPath("geometry").description("3D 모델 모양"),
                                fieldWithPath("material").description("3D 모델 색상")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("수정된 " + STUFF_ID.getDescription())))
                ));

    }

    @Test
    public void 특정stuff_삭제() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/stuffs/{stuffId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("stuff-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stuffId").description("삭제할 stuff의 id")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("삭제된 "+STUFF_ID.getDescription())))
                ));
    }

    private List<FieldDescriptor> getStuffResponse() {
        return Arrays.asList(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description(STUFF_ID.getDescription()),
                fieldWithPath("stuffName").type(JsonFieldType.STRING).description(STUFF_NAME.getDescription()),
                fieldWithPath("stuffNameKor").type(JsonFieldType.STRING).description(STUFF_NAME_KOR.getDescription()),
                fieldWithPath("price").type(JsonFieldType.NUMBER).description(STUFF_PRICE.getDescription()),
                fieldWithPath("categories").type(JsonFieldType.ARRAY).description(STUFF_CATEGORIES.getDescription()),
                fieldWithPath("categories.[].category").type(JsonFieldType.STRING).description(CATEGORY.getDescription()),
                fieldWithPath("categories.[].categoryName").type(JsonFieldType.STRING).description(CATEGORY_NAME.getDescription()),
                fieldWithPath("stuffType").type(JsonFieldType.STRING).description(STUFF_TYPE.getDescription()),
                fieldWithPath("geometry").type(JsonFieldType.STRING).description(GEOMETRY.getDescription()),
                fieldWithPath("material").type(JsonFieldType.STRING).description(MATERIAL.getDescription()),
                fieldWithPath("stuffGlbPath").type(JsonFieldType.STRING).description(STUFF_GLB_PATH.getDescription()),
                fieldWithPath("createdTime").type(JsonFieldType.STRING).description(STUFF_CREATED.getDescription()).ignored(),
                fieldWithPath("modifiedTime").type(JsonFieldType.STRING).description(STUFF_MODIFIED.getDescription()).ignored()
        );
    }


}
