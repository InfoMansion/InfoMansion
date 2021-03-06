package com.infomansion.server.domain.userstuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.dto.UserStuffIncludeRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffModifyRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffPositionRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserStuffApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private UserStuffService userStuffService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private List<Long> stuffIds;

    @BeforeEach
    public void setUp() {
        // user ??????
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String categories = "IT,COOK";

        userId = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(categories)
                .build()).getId();

        // stuff ??????
        stuffIds = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            String stuffName = "notebook"+(i+1);
            String stuffNameKor = "?????????"+(i+1);
            Long price = 30L;
            String category = "IT";
            String stuffType = "STUFF";

            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName(stuffName)
                    .stuffNameKor(stuffNameKor)
                    .price(price)
                    .category(category)
                    .stuffType(stuffType)
                    .build();

            stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
        }
    }


    @DisplayName("???????????? ?????? user_id ?????? stuff_id??? UserStuff ?????? ??????")
    @Test
    public void userstuff_??????_??????() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(100000L)
                .stuffId(stuffIds.get(0)).build();

        String createDtoJson = objectMapper.writeValueAsString(createDto);

        // when, then
        mockMvc.perform(post("/api/v1/userstuffs")
                        .content(createDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("???????????? ?????? ??? ????????????."))
                .andExpect(jsonPath("$.code").value(40001));
    }


    @DisplayName("???????????? ?????? userStuffId??? UserStuff ?????? ??? ??????")
    @Test
    public void userstuff_??????_??????_1() throws Exception {
        // given
        Long userStuffId = 9999L;

        // when, then
        mockMvc.perform(get("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("???????????? ?????? Stuff?????????."))
                .andExpect(jsonPath("$.code").value(40060));
    }


    @DisplayName("???????????? ?????? user_id??? UserStuff ?????? ?????? ??? ??????")
    @Test
    public void userstuff_??????_??????_2() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();

        // when, then
        mockMvc.perform(get("/api/v1/userstuffs/list/"+(userId+99999)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("???????????? ?????? ??? ????????????."))
                .andExpect(jsonPath("$.code").value(40001));
    }


    @DisplayName("???????????? ?????? userStuffId??? ?????? ?????? ??? ??????")
    @Test
    public void userstuff_??????_??????_1() throws Exception {
        // given
        Long userStuffId = 99999L;

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("40060"))
                .andExpect(jsonPath("$.message").value("???????????? ?????? Stuff?????????."));
    }

    @DisplayName("?????? ????????? userStuffId??? ?????? ?????? ??? ??????")
    @Test
    public void userstuff_??????_??????_2() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("40061"))
                .andExpect(jsonPath("$.message").value("????????? Stuff?????????."));

    }

    @DisplayName("?????? ????????? userStuffId??? ?????? ?????? ??? ??????")
    @Test
    public void userstuff_??????_??????() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java ??????").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        String includeDtoJson = objectMapper.writeValueAsString(includeDto);
        mockMvc.perform(put("/api/v1/userstuffs")
                        .content(includeDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // when, then
        UserStuffIncludeRequestDto reIncludeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("GAME ?????? ??????").category("GAME")
                .posX(3.2).posY(3.3).posZ(0.1)
                .rotX(0.0).rotY(0.0).rotZ(1.2)
                .build();
        String reIncludeDtoJson = objectMapper.writeValueAsString(reIncludeDto);
        mockMvc.perform(put("/api/v1/userstuffs")
                        .content(reIncludeDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(40062))
                .andExpect(jsonPath("$.message").value("????????? Stuff?????????."));

    }

    @DisplayName("alias, category ??? ??? ????????? ??? ??????")
    @Test
    public void userstuff_alias_and_category_??????_??????() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java ??????").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();

        String includeDtoJson = objectMapper.writeValueAsString(includeDto);
        mockMvc.perform(put("/api/v1/userstuffs")
                        .content(includeDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // when, then
        UserStuffModifyRequestDto modifyDto = UserStuffModifyRequestDto.builder()
                .id(userStuffId).build();
        String modifyDtoJson = objectMapper.writeValueAsString(modifyDto);
        mockMvc.perform(put("/api/v1/userstuffs/option")
                        .content(modifyDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(40063))
                .andExpect(jsonPath("$.message").value("?????? ?????? ???????????? ?????? ???????????????."));

    }

    @DisplayName("???????????? ?????? UserStuff??? Position ?????? ??? ??????")
    @Test
    public void userstuff_pos_and_rot_??????_??????() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        // when, then
        UserStuffPositionRequestDto modifyDto = UserStuffPositionRequestDto.builder()
                .id(userStuffId)
                .posX(3.2).posY(3.3).posZ(0.1)
                .rotX(0.0).rotY(0.0).rotZ(1.2).build();
        String modifyDtoJson = objectMapper.writeValueAsString(modifyDto);
        mockMvc.perform(put("/api/v1/userstuffs/position")
                        .content(modifyDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(40061))
                .andExpect(jsonPath("$.message").value("????????? Stuff?????????."));
    }


}

