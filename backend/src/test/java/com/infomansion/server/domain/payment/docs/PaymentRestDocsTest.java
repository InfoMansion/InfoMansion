package com.infomansion.server.domain.payment.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.payment.dto.PaymentLineResponseDto;
import com.infomansion.server.domain.payment.dto.PaymentResponseDto;
import com.infomansion.server.domain.payment.service.PaymentService;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.FieldDescription.*;
import static com.infomansion.server.global.util.restdocs.FieldDescription.SLICE_SIZE;
import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
public class PaymentRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void 로그인한_사용자의_결제내역_조회() throws Exception {
        //given
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
        StuffResponseDto stuffResponseDto = new StuffResponseDto(stuff);
        List<PaymentLineResponseDto> paymentLines = new ArrayList<>();
        for(int i=1;i<=3;i++) {
            paymentLines.add(
                    PaymentLineResponseDto.builder()
                            .id((long) i)
                            .price((long) i * 100L)
                            .stuff(stuffResponseDto)
                            .build()
            );
        }
        List<PaymentResponseDto> responseDto = new ArrayList<>();
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .id(1L)
                .beforeCredit(1000L)
                .afterCredit(400L)
                .totalPrice(600L)
                .paymentDate(LocalDateTime.now())
                .paymentLines(paymentLines)
                .build();
        responseDto.add(paymentResponseDto);

        given(paymentService.findPaymentLinesByLoginUser(anyInt())).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page","1"))
                .andExpect(status().isOk())
                .andDo(document("payment-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지의 번호")
                        ),
                        relaxedResponseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("로그인한 유저의 최근 10건의 결제 내역")))
                                .andWithPrefix("data.",
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("결제 id"),
                                        fieldWithPath("[].beforeCredit").type(JsonFieldType.NUMBER).description("해당 결제를 진행하기 전 User의 크레딧"),
                                        fieldWithPath("[].afterCredit").type(JsonFieldType.NUMBER).description("해당 결제를 진행한 후 User의 크레딧"),
                                        fieldWithPath("[].totalPrice").type(JsonFieldType.NUMBER).description("결제의 총 금액"),
                                        fieldWithPath("[].paymentDate").type(JsonFieldType.STRING).description("결제 일"),
                                        fieldWithPath("[].paymentLines").type(JsonFieldType.ARRAY).description("해당 결제 안의 결제 내역들"),
                                        fieldWithPath("[].paymentLines.[].id").type(JsonFieldType.NUMBER).description("단 건의 결제 내역 id"),
                                        fieldWithPath("[].paymentLines.[].price").type(JsonFieldType.NUMBER).description("단 건의 결제 금액"),
                                        fieldWithPath("[].paymentLines.[].stuff.id").type(JsonFieldType.NUMBER).description("구매한 Stuff의 id"),
                                        fieldWithPath("[].paymentLines.[].stuff.stuffName").type(JsonFieldType.STRING).description("구매한 Stuff의 id"),
                                        fieldWithPath("[].paymentLines.[].stuff.stuffNameKor").type(JsonFieldType.STRING).description("구매한 Stuff의 id"),
                                        fieldWithPath("[].paymentLines.[].stuff.price").type(JsonFieldType.NUMBER).description("구매한 Stuff의 id"),
                                        fieldWithPath("[].paymentLines.[].stuff.categories").type(JsonFieldType.ARRAY).description(STUFF_CATEGORIES.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.categories.[].category").type(JsonFieldType.STRING).description(CATEGORY.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.categories.[].categoryName").type(JsonFieldType.STRING).description(CATEGORY_NAME.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.id").type(JsonFieldType.NUMBER).description("구매한 Stuff의 id"),
                                        fieldWithPath("[].paymentLines.[].stuff.stuffType").type(JsonFieldType.STRING).description(STUFF_TYPE.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.geometry[]").type(JsonFieldType.ARRAY).description(GEOMETRY.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.material[]").type(JsonFieldType.ARRAY).description(MATERIAL.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.stuffGlbPath").type(JsonFieldType.STRING).description(STUFF_GLB_PATH.getDescription()),
                                        fieldWithPath("[].paymentLines.[].stuff.createdTime").type(JsonFieldType.STRING).description(STUFF_CREATED.getDescription()).ignored(),
                                        fieldWithPath("[].paymentLines.[].stuff.modifiedTime").type(JsonFieldType.STRING).description(STUFF_MODIFIED.getDescription()).ignored()                               )
                ));
    }
}
