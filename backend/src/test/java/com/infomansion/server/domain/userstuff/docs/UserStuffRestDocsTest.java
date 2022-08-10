package com.infomansion.server.domain.userstuff.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
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
        UserStuffSaveRequestDto requestDto = UserStuffSaveRequestDto.builder()
                .stuffId(999L).build();
        given(userStuffService.saveUserStuff(any(UserStuffSaveRequestDto.class))).willReturn(userStuffId);

        // when, then
        mockMvc.perform(post("/api/v1/userstuffs/list")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("userstuff-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
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
                .stuffGlbPath("stuffGlbPath")
                .build();
        UserStuffResponseDto responseDto = UserStuffResponseDto.toDto(
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
                                .andWithPrefix("data.",
                                        fieldWithPath("userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description(USERSTUFF_ID.getDescription()),
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
                                        fieldWithPath("geometry[]").type(GEOMETRY.getJsonFieldType()).description(GEOMETRY.getDescription()),
                                        fieldWithPath("material[]").type(MATERIAL.getJsonFieldType()).description(MATERIAL.getDescription()),
                                        fieldWithPath("stuffGlbPath").type(STUFF_GLB_PATH.getJsonFieldType()).description(STUFF_GLB_PATH.getDescription()).optional(),
                                        fieldWithPath("createdTime").type(USERSTUFF_SELECTED.getJsonFieldType()).description(USERSTUFF_CREATED.getDescription()).optional(),
                                        fieldWithPath("modifiedTime").type(USERSTUFF_MODIFIED.getJsonFieldType()).description(USERSTUFF_MODIFIED.getDescription()).optional()
                                )
                ));
    }

    @Test
    public void 로그인된_사용자의_모든_userStuff_조회() throws Exception {
        // given
        List<UserStuffEditResponseDto> responseDtoList = new ArrayList<>();
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
            responseDtoList.add(UserStuffEditResponseDto.toDto(
                    UserStuff.builder()
                            .id(Long.valueOf(i))
                            .stuff(stuff)
                            .alias("info팀의 Daily")
                            .category(Category.DAILY)
                            .selected(true)
                            .posX(BigDecimal.ONE).posY(BigDecimal.ONE).posZ(BigDecimal.ONE)
                            .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO).build()));
        }
        given(userStuffService.findAllUserStuff()).willReturn(responseDtoList);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/userstuffs/list"))
                .andExpect(status().isOk())
                .andDo(document("userstuff-list-with-token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용자의 모든 UserStuff")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description(USERSTUFF_ID.getDescription()),
                                        fieldWithPath("stuffType").type(STUFF_TYPE.getJsonFieldType()).description(STUFF_TYPE.getDescription()),
                                        fieldWithPath("alias").type(ALIAS.getJsonFieldType()).description(ALIAS.getDescription()),
                                        fieldWithPath("selectedCategory").type(JsonFieldType.STRING).description("선택된 카테고리"),
                                        fieldWithPath("categories[]").type(JsonFieldType.ARRAY).description("Category"),
                                        fieldWithPath("categories.[].category").type(CATEGORY.getJsonFieldType()).description(CATEGORY.getDescription()),
                                        fieldWithPath("categories.[].categoryName").type(CATEGORY_NAME.getJsonFieldType()).description(CATEGORY_NAME.getDescription()),
                                        fieldWithPath("selected").type(USERSTUFF_SELECTED.getJsonFieldType()).description(USERSTUFF_SELECTED.getDescription()),
                                        fieldWithPath("posX").type(POS_X.getJsonFieldType()).description(POS_X.getDescription()),
                                        fieldWithPath("posY").type(POS_Y.getJsonFieldType()).description(POS_Y.getDescription()),
                                        fieldWithPath("posZ").type(POS_Z.getJsonFieldType()).description(POS_Z.getDescription()),
                                        fieldWithPath("rotX").type(ROT_X.getJsonFieldType()).description(ROT_X.getDescription()),
                                        fieldWithPath("rotY").type(ROT_Y.getJsonFieldType()).description(ROT_Y.getDescription()),
                                        fieldWithPath("rotZ").type(ROT_Z.getJsonFieldType()).description(ROT_Z.getDescription()),
                                        fieldWithPath("geometry[]").type(GEOMETRY.getJsonFieldType()).description(GEOMETRY.getDescription()),
                                        fieldWithPath("material[]").type(MATERIAL.getJsonFieldType()).description(MATERIAL.getDescription()),
                                        fieldWithPath("stuffGlbPath").type(STUFF_GLB_PATH.getJsonFieldType()).description(STUFF_GLB_PATH.getDescription()).optional(),
                                        fieldWithPath("createdTime").type(USERSTUFF_SELECTED.getJsonFieldType()).description(USERSTUFF_CREATED.getDescription()).optional(),
                                        fieldWithPath("modifiedTime").type(USERSTUFF_MODIFIED.getJsonFieldType()).description(USERSTUFF_MODIFIED.getDescription()).optional()
                                )
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

    @Test
    public void username으로_방에_배치된_userstuff_조회() throws Exception {
        // given
        String username = "infomansion";
        List<StuffType> list = Arrays.asList(StuffType.WALL, StuffType.FLOOR, StuffType.DESK, StuffType.CLOSET, StuffType.DRAWER);
        List<UserStuffArrangedResponseDto> responseDtoList = new ArrayList<>();
        for(int i = 1; i < 5; i+=2) {
            String categories = "DAILY,STUDY,INTERIOR";
            Stuff stuff = Stuff.builder()
                    .id(10L+i).stuffName("desk"+i).stuffNameKor("책상"+i).price(50L+i)
                    .categories(categories).stuffType(list.get(i))
                    .geometry("geometry").material("material").stuffGlbPath("glbPath")
                    .build();
            responseDtoList.add(UserStuffArrangedResponseDto.toDto(
                    UserStuff.builder()
                            .id(Long.valueOf(i))
                            .stuff(stuff)
                            .alias("info팀의 Daily")
                            .category(Category.DAILY)
                            .posX(BigDecimal.ONE).posY(BigDecimal.ONE).posZ(BigDecimal.ONE)
                            .rotX(BigDecimal.ZERO).rotY(BigDecimal.ZERO).rotZ(BigDecimal.ZERO).build()));
        }
        given(userStuffService.findArrangedUserStuffByUsername(any(String.class))).willReturn(responseDtoList);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/userstuffs/room/{username}", username))
                .andExpect(status().isOk())
                .andDo(document("userstuff-get-arranged-userstuff-with-username",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("username").description("사용자 이름")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용자 방에 배치된 모든 UserStuff")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("id").type(USERSTUFF_ID.getJsonFieldType()).description(USERSTUFF_ID.getDescription()),
                                        fieldWithPath("stuffNameKor").type(STUFF_NAME_KOR.getJsonFieldType()).description(STUFF_NAME_KOR.getDescription()),
                                        fieldWithPath("alias").type(ALIAS.getJsonFieldType()).description(ALIAS.getDescription()),
                                        fieldWithPath("category").type(JsonFieldType.OBJECT).description("Category"),
                                        fieldWithPath("category.category").type(CATEGORY.getJsonFieldType()).description(CATEGORY.getDescription()),
                                        fieldWithPath("category.categoryName").type(CATEGORY_NAME.getJsonFieldType()).description(CATEGORY_NAME.getDescription()),
                                        fieldWithPath("posX").type(POS_X.getJsonFieldType()).description(POS_X.getDescription()),
                                        fieldWithPath("posY").type(POS_Y.getJsonFieldType()).description(POS_Y.getDescription()),
                                        fieldWithPath("posZ").type(POS_Z.getJsonFieldType()).description(POS_Z.getDescription()),
                                        fieldWithPath("rotX").type(ROT_X.getJsonFieldType()).description(ROT_X.getDescription()),
                                        fieldWithPath("rotY").type(ROT_Y.getJsonFieldType()).description(ROT_Y.getDescription()),
                                        fieldWithPath("rotZ").type(ROT_Z.getJsonFieldType()).description(ROT_Z.getDescription())
                                )
                ));
    }

    @Test
    public void userStuff_구매() throws Exception{
        // given
        UserStuffPurchaseRequestDto requestDto = new UserStuffPurchaseRequestDto(List.of(1L,2L,3L,4L,5L));
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

        given(userStuffService.purchaseStuff(any(UserStuffPurchaseRequestDto.class))).willReturn(responseDtoList);

        // when, then
        mockMvc.perform(post("/api/v2/user-stuff")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("user-stuff-purchase",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("stuffIds").description("구매할 Stuff들의 id들")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("구매한 Stuff들에 대한 정보")))
                                .andWithPrefix("data.[].",getStuffResponse())
                ));
    }

    @Test
    public void 배치된_userSutff의_카테고리_조회() throws Exception {
        // given
        List<UserStuffCategoryResponseDto> response = new ArrayList<>();
        for(int i = 1; i <= 3; i++) {
            response.add(UserStuffCategoryResponseDto.builder()
                    .userStuffId(Long.valueOf(i))
                    .alias("데일리"+i).category(new CategoryMapperValue(Category.DAILY)).build());
        }
        given(userStuffService.findCategoryPlacedInRoom()).willReturn(response);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/userstuffs/category"))
                .andExpect(status().isOk())
                .andDo(document("userstuff-category-placed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("배치된 UserStuff의 카테고리")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description(USERSTUFF_ID.getDescription()),
                                        fieldWithPath("alias").type(ALIAS.getJsonFieldType()).description(ALIAS.getDescription()),
                                        fieldWithPath("category").type(JsonFieldType.OBJECT).description("카테고리 정보"),
                                        fieldWithPath("category.category").type(CATEGORY.getJsonFieldType()).description(CATEGORY.getDescription()),
                                        fieldWithPath("category.categoryName").type(CATEGORY_NAME.getJsonFieldType()).description(CATEGORY_NAME.getDescription())
                                )
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
                fieldWithPath("geometry[]").type(JsonFieldType.ARRAY).description(GEOMETRY.getDescription()),
                fieldWithPath("material[]").type(JsonFieldType.ARRAY).description(MATERIAL.getDescription()),
                fieldWithPath("stuffGlbPath").type(JsonFieldType.STRING).description(STUFF_GLB_PATH.getDescription()),
                fieldWithPath("createdTime").type(JsonFieldType.STRING).description(STUFF_CREATED.getDescription()).ignored(),
                fieldWithPath("modifiedTime").type(JsonFieldType.STRING).description(STUFF_MODIFIED.getDescription()).ignored()
        );
    }

    @Test
    public void room_편집_완료_시_변경된_userStuff를_저장() throws Exception {
        // given
        List<UserStuffEditRequestDto> requestDtoList = new ArrayList<>();
        for(int i = 1; i < 5; i+=2) {
            requestDtoList.add( new UserStuffEditRequestDto(Long.valueOf(i), "alias", "DAILY",
                    0.5, 0.5 ,0.5, 0.5, 0.5, 0.5));
        }
        given(userStuffService.editUserStuff(any(List.class))).willReturn(true);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/userstuffs/edit")
                        .content(objectMapper.writeValueAsString(requestDtoList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("userstuff-edit-room",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("[]").description("배치할 UserStuff"),
                                fieldWithPath("[].userStuffId").type(USERSTUFF_ID.getJsonFieldType()).description(USERSTUFF_ID.getDescription()),
                                fieldWithPath("[].alias").type(ALIAS.getJsonFieldType()).description(ALIAS.getDescription()),
                                fieldWithPath("[].selectedCategory").type(JsonFieldType.STRING).description("선택된 카테고리"),
                                fieldWithPath("[].posX").type(POS_X.getJsonFieldType()).description(POS_X.getDescription()),
                                fieldWithPath("[].posY").type(POS_Y.getJsonFieldType()).description(POS_Y.getDescription()),
                                fieldWithPath("[].posZ").type(POS_Z.getJsonFieldType()).description(POS_Z.getDescription()),
                                fieldWithPath("[].rotX").type(ROT_X.getJsonFieldType()).description(ROT_X.getDescription()),
                                fieldWithPath("[].rotY").type(ROT_Y.getJsonFieldType()).description(ROT_Y.getDescription()),
                                fieldWithPath("[].rotZ").type(ROT_Z.getJsonFieldType()).description(ROT_Z.getDescription())
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("UserStuff 편집 성공")))
                ));
    }
}
