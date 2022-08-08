package com.infomansion.server.domain.notification.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.notification.domain.NotificationType;
import com.infomansion.server.domain.notification.dto.NotificationResponseDto;
import com.infomansion.server.domain.notification.dto.UnReadNotificationResponseDto;
import com.infomansion.server.domain.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.infomansion.server.global.util.restdocs.RestDocsUtil.common;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@SpringBootTest
class NotificationRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void 사용자가_읽지않은_Notification_목록_조회() throws Exception {
        //given
        List<UnReadNotificationResponseDto> responseDtoList = new ArrayList<>();
        for(int i=1;i<=5;i++) {
            String ntype = "";

            if(i == 2 || i == 4) ntype = NotificationType.FOLLOW_USER.getNtype();
            else if(i == 3) ntype = NotificationType.GUEST_BOOK.getNtype();
            else ntype = NotificationType.LIKE_POST.getNtype();

            responseDtoList.add(UnReadNotificationResponseDto.builder()
                    .id((long) i)
                    .fromUsername("infomansion"+i)
                    .targetId((long) i)
                    .ntype(ntype)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        given(notificationService.findSimpleUnReadNotifications()).willReturn(responseDtoList);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andDo(document("notifications-user-unread",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("안 읽은 Notifications")))
                                .andWithPrefix("data.[].",
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Notification id"),
                                fieldWithPath("fromUsername").type(JsonFieldType.STRING).description("나를 팔로우하거나 내 Post에 좋아요를 누른 다른 사용자의 username"),
                                fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("좋아요가 눌러진 Post의 id 혹은 나를 팔로우한 User의 id"),
                                fieldWithPath("ntype").type(JsonFieldType.STRING).description("Notification의 타입 [팔로우, Post 좋아요, 방명록 글 남김]"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("Notification의 생성 시간")
                        )
                        ));
    }

    @Test
    public void 사용자의_모든_Notifications_조회_페이징() throws Exception {
        //given
        List<NotificationResponseDto> responseDtoList = new ArrayList<>();
        for(int i=1;i<=5;i++) {
            String ntype = "";
            boolean isRead = false;

            if(i == 2 || i == 4) {
                ntype = NotificationType.FOLLOW_USER.getNtype();
                isRead = true;
            }
            else if(i == 3) ntype = NotificationType.GUEST_BOOK.getNtype();
            else ntype = NotificationType.LIKE_POST.getNtype();

            responseDtoList.add(NotificationResponseDto.builder()
                    .id((long) i)
                    .fromUsername("infomansion"+i)
                    .targetId((long) i)
                    .ntype(ntype)
                    .createdDate(LocalDateTime.now())
                            .isRead(isRead)
                    .build());
        }
        Integer pageNum = 5;
        given(notificationService.findAllNotificationsPagable(anyInt())).willReturn(responseDtoList);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notifications/all")
                        .param("pageNum", String.valueOf(pageNum)))
                .andExpect(status().isOk())
                .andDo(document("notifications-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("pageNum").description("페이지 넘버")
                        ),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.ARRAY).description("모든 Notifications")))
                                .andWithPrefix("data.[].",
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Notification id"),
                                        fieldWithPath("fromUsername").type(JsonFieldType.STRING).description("나를 팔로우하거나 내 Post에 좋아요를 누른 다른 사용자의 username"),
                                        fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("좋아요가 눌러진 Post의 id 혹은 나를 팔로우한 User의 id"),
                                        fieldWithPath("ntype").type(JsonFieldType.STRING).description("Notification의 타입 [팔로우, Post 좋아요, 방명록 글 남김]"),
                                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("Notification의 생성 시간"),
                                        fieldWithPath("read").type(JsonFieldType.BOOLEAN).description("해당 Notification을 읽었는지 여부")
                                )
                ));
    }

    @Test
    public void 읽지않은_Notification_읽음처리() throws Exception {
        //given
        List<UnReadNotificationResponseDto> responseDtoList = new ArrayList<>();
        for(int i=1;i<=5;i++) {
            String ntype = "";

            if(i == 2 || i == 4) ntype = NotificationType.FOLLOW_USER.getNtype();
            else if(i == 3) ntype = NotificationType.GUEST_BOOK.getNtype();
            else ntype = NotificationType.LIKE_POST.getNtype();

            responseDtoList.add(UnReadNotificationResponseDto.builder()
                    .id((long) i)
                    .fromUsername("infomansion"+i)
                    .targetId((long) i)
                    .ntype(ntype)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        given(notificationService.readUnReadNotifications()).willReturn(5);

        //when & then
        mockMvc.perform(post("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andDo(document("read-unread-notifications",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(common(fieldWithPath("data").type(JsonFieldType.NUMBER).description("읽은 알람 개수")))
                        ));

    }

}