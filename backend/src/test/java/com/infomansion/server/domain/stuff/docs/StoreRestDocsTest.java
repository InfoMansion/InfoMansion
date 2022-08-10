package com.infomansion.server.domain.stuff.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StoreGroupResponseDto;
import com.infomansion.server.domain.stuff.dto.StoreResponseDto;
import com.infomansion.server.domain.stuff.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class StoreRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    @Test
    public void 모든가구타입에_대한_stuff_조회() throws Exception {
        // given
        List<StoreGroupResponseDto> responseList = new ArrayList<>();
        List<StoreResponseDto> content1 = new ArrayList<>();
        content1.add(new StoreResponseDto(1L, "책상1", 10L, "geometry", "material", "glbPath"));
        content1.add(new StoreResponseDto(2L, "책상2", 10L, "geometry", "material", "glbPath"));
        content1.add(new StoreResponseDto(3L, "책상3", 10L, "geometry", "material", "glbPath"));
        StoreGroupResponseDto desk = new StoreGroupResponseDto(StuffType.DESK, new SliceImpl<>(content1, Pageable.ofSize(3), false));
        responseList.add(desk);

        List<StoreResponseDto> content2 = new ArrayList<>();
        content2.add(new StoreResponseDto(10L, "의자1", 5L, "geometry", "material", "glbPath"));
        content2.add(new StoreResponseDto(11L, "의자2", 5L, "geometry", "material", "glbPath"));
        content2.add(new StoreResponseDto(13L, "의자4", 5L, "geometry", "material", "glbPath"));
        StoreGroupResponseDto chair = new StoreGroupResponseDto(StuffType.CHAIR, new SliceImpl<>(content2, Pageable.ofSize(3), true));
        responseList.add(chair);

        given(storeService.findAllStuffInStore(3)).willReturn(responseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stores?pageSize=3"))
                .andExpect(status().isOk())
                .andDo(document("store-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("pageSize").description("한 페이지에 나타낼 가구 수")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("페이징된 결과")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("stuffType").type(STUFF_TYPE.getJsonFieldType()).description(STUFF_TYPE.getDescription()),
                                        fieldWithPath("stuffTypeName").type(STUFF_TYPE_NAME.getJsonFieldType()).description(STUFF_TYPE_NAME.getDescription()),
                                        fieldWithPath("slice").type(SLICE.getJsonFieldType()).description(SLICE.getDescription()),
                                        fieldWithPath("slice.content[]").type(SLICE_CONTENT.getJsonFieldType()).description(SLICE_CONTENT.getDescription()),
                                        fieldWithPath("slice.content.[].id").type(STUFF_ID.getJsonFieldType()).description(STUFF_ID.getDescription()),
                                        fieldWithPath("slice.content.[].stuffNameKor").type(STUFF_NAME_KOR.getJsonFieldType()).description(STUFF_NAME_KOR.getDescription()),
                                        fieldWithPath("slice.content.[].price").type(STUFF_PRICE.getJsonFieldType()).description(STUFF_PRICE.getDescription()),
                                        fieldWithPath("slice.content.[].geometry[]").type(GEOMETRY.getJsonFieldType()).description(GEOMETRY.getDescription()),
                                        fieldWithPath("slice.content.[].material[]").type(MATERIAL.getJsonFieldType()).description(MATERIAL.getDescription()),
                                        fieldWithPath("slice.content.[].stuffGlbPath").type(STUFF_GLB_PATH.getJsonFieldType()).description(STUFF_GLB_PATH.getDescription()),
                                        fieldWithPath("slice.numberOfElements").type(SLICE_NUMBER_OF_ELEMENTS.getJsonFieldType()).description(SLICE_NUMBER_OF_ELEMENTS.getDescription()),
                                        fieldWithPath("slice.first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("slice.last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("slice.number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("slice.size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())
                                        )
                ));

    }

    @Test
    public void 특정가구타입에_대한_stuff_조회() throws Exception {
        // given
        List<StoreResponseDto> content = new ArrayList<>();
        content.add(new StoreResponseDto(4L, "책상4", 15L, "geometry", "material", "glbPath"));
        content.add(new StoreResponseDto(5L, "책상5", 10L, "geometry", "material", "glbPath"));
        content.add(new StoreResponseDto(6L, "책상6", 12L, "geometry", "material", "glbPath"));
        Slice<StoreResponseDto> response = new SliceImpl<>(content, Pageable.ofSize(3).withPage(1), true);

        given(storeService.findStuffWithStuffTypeInStore(StuffType.DESK, Pageable.ofSize(3).withPage(1))).willReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/stores/{stuffType}?page=1&size=3", "DESK"))
                .andExpect(status().isOk())
                .andDo(document("store-with-stufftype",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stuffType").description("조회할 가구타입")
                        ),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지에 나타낼 가구 수")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.OBJECT).description("조회된 Stuff")))
                                .andWithPrefix("data.",
                                        fieldWithPath("content[]").type(SLICE_CONTENT.getJsonFieldType()).description(SLICE_CONTENT.getDescription()),
                                        fieldWithPath("content.[].id").type(STUFF_ID.getJsonFieldType()).description(STUFF_ID.getDescription()),
                                        fieldWithPath("content.[].stuffNameKor").type(STUFF_NAME_KOR.getJsonFieldType()).description(STUFF_NAME_KOR.getDescription()),
                                        fieldWithPath("content.[].price").type(STUFF_PRICE.getJsonFieldType()).description(STUFF_PRICE.getDescription()),
                                        fieldWithPath("content.[].geometry[]").type(GEOMETRY.getJsonFieldType()).description(GEOMETRY.getDescription()),
                                        fieldWithPath("content.[].material[]").type(MATERIAL.getJsonFieldType()).description(MATERIAL.getDescription()),
                                        fieldWithPath("content.[].stuffGlbPath").type(STUFF_GLB_PATH.getJsonFieldType()).description(STUFF_GLB_PATH.getDescription()),
                                        fieldWithPath("numberOfElements").type(SLICE_NUMBER_OF_ELEMENTS.getJsonFieldType()).description(SLICE_NUMBER_OF_ELEMENTS.getDescription()),
                                        fieldWithPath("first").type(SLICE_FIRST.getJsonFieldType()).description(SLICE_FIRST.getDescription()),
                                        fieldWithPath("last").type(SLICE_LAST.getJsonFieldType()).description(SLICE_LAST.getDescription()),
                                        fieldWithPath("number").type(SLICE_NUMBER.getJsonFieldType()).description(SLICE_NUMBER.getDescription()),
                                        fieldWithPath("size").type(SLICE_SIZE.getJsonFieldType()).description(SLICE_SIZE.getDescription())
                                        )
                ));
    }

    @Test
    public void 최신_stuff_조회() throws Exception {
        // given
        List<StoreResponseDto> response = new ArrayList<>();
        response.add(new StoreResponseDto(4L, "책상4", 15L, "geometry", "material", "glbPath"));
        response.add(new StoreResponseDto(5L, "책상5", 10L, "geometry", "material", "glbPath"));
        response.add(new StoreResponseDto(6L, "책상6", 12L, "geometry", "material", "glbPath"));
        given(storeService.findTheLatestStuff()).willReturn(response);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/stores/latest"))
                .andExpect(status().isOk())
                .andDo(document("ths-latest-stuff",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("최신 Stuff")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("id").type(STUFF_ID.getJsonFieldType()).description(STUFF_ID.getDescription()),
                                        fieldWithPath("stuffNameKor").type(STUFF_NAME_KOR.getJsonFieldType()).description(STUFF_NAME_KOR.getDescription()),
                                        fieldWithPath("price").type(STUFF_PRICE.getJsonFieldType()).description(STUFF_PRICE.getDescription()),
                                        fieldWithPath("geometry[]").type(GEOMETRY.getJsonFieldType()).description(GEOMETRY.getDescription()),
                                        fieldWithPath("material[]").type(MATERIAL.getJsonFieldType()).description(MATERIAL.getDescription()),
                                        fieldWithPath("stuffGlbPath").type(STUFF_GLB_PATH.getJsonFieldType()).description(STUFF_GLB_PATH.getDescription())
                                )
                ));
    }
}
