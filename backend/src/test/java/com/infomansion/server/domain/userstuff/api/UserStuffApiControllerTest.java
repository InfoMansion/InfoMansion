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
import com.infomansion.server.global.util.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UserStuffRepository userStuffRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private List<Long> stuffIds;

    @BeforeEach
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String uCategories = "IT,COOK";

        userId = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build()).getId();

        // stuff 생성
        stuffIds = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            String stuffName = "notebook"+(i+1);
            String stuffNameKor = "노트북"+(i+1);
            Long price = 30L;
            String categories = "IT";
            String stuffType = "STUFF";

            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName(stuffName)
                    .stuffNameKor(stuffNameKor)
                    .price(price)
                    .categories(categories)
                    .stuffType(stuffType)
                    .geometry("geometry")
                    .materials("materials")
                    .build();

            stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
        }
    }

    @AfterEach
    public void reset() {
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }


    @DisplayName("유효하지 않은 user_id 또는 stuff_id로 UserStuff 생성 실패")
    @Test
    public void userstuff_생성_실패() throws Exception {
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
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
    }


    @DisplayName("유효하지 않은 userStuffId로 UserStuff 조회 시 실패")
    @Test
    public void userstuff_조회_실패_1() throws Exception {
        // given
        Long userStuffId = 9999L;

        // when, then
        mockMvc.perform(get("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_STUFF_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_STUFF_NOT_FOUND.getCode()));
    }


    @DisplayName("유효하지 않은 user_id로 UserStuff 목록 조회 시 실패")
    @Test
    public void userstuff_조회_실패_2() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();

        // when, then
        mockMvc.perform(get("/api/v1/userstuffs/list/"+(userId+99999)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
    }


    @DisplayName("유효하지 않은 userStuffId로 제외 요청 시 실패")
    @Test
    public void userstuff_제외_실패_1() throws Exception {
        // given
        Long userStuffId = 99999L;

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_STUFF_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_STUFF_NOT_FOUND.getMessage()));
    }

    @DisplayName("이미 제외된 userStuffId로 제외 요청 시 실패")
    @Test
    public void userstuff_제외_실패_2() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        // when, then
        mockMvc.perform(put("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.EXCLUDED_USER_STUFF.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.EXCLUDED_USER_STUFF.getMessage()));

    }

    @DisplayName("이미 배치된 userStuffId로 배치 요청 시 실패")
    @Test
    public void userstuff_배치_실패() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
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
                .id(userStuffId).alias("GAME 공략 모음").category("GAME")
                .posX(3.2).posY(3.3).posZ(0.1)
                .rotX(0.0).rotY(0.0).rotZ(1.2)
                .build();
        String reIncludeDtoJson = objectMapper.writeValueAsString(reIncludeDto);
        mockMvc.perform(put("/api/v1/userstuffs")
                        .content(reIncludeDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INCLUDED_USER_STUFF.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INCLUDED_USER_STUFF.getMessage()));

    }

    @DisplayName("alias, category 둘 다 미입력 시 실패")
    @Test
    public void userstuff_alias_and_category_수정_실패() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
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
                .andExpect(jsonPath("$.code").value(ErrorCode.NULL_VALUE_OF_ALIAS_AND_CATEGORY.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NULL_VALUE_OF_ALIAS_AND_CATEGORY.getMessage()));

    }

    @DisplayName("유효하지 않은 category 입력 시 실패")
    @Test
    public void userstuff_category_수정_실패() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffIncludeRequestDto includeDto = UserStuffIncludeRequestDto.builder()
                .id(userStuffId).alias("Java 정리").category("IT")
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
                .id(userStuffId).category("NEWS").build();
        String modifyDtoJson = objectMapper.writeValueAsString(modifyDto);
        mockMvc.perform(put("/api/v1/userstuffs/option")
                        .content(modifyDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_VALID_CATEGORY.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_VALID_CATEGORY.getMessage()));

    }

    @DisplayName("배치되지 않은 UserStuff의 Position 변경 시 실패")
    @Test
    public void userstuff_pos_and_rot_수정_실패() throws Exception {
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
                .andExpect(jsonPath("$.code").value(ErrorCode.EXCLUDED_USER_STUFF.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.EXCLUDED_USER_STUFF.getMessage()));
    }

    @DisplayName("유효하지 않은 userStuffId로 삭제 요청 시 실패")
    @Test
    public void userstuff_삭제_실패() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        // when, then
        mockMvc.perform(patch("/api/v1/userstuffs/"+(userStuffId+999999)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_STUFF_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_STUFF_NOT_FOUND.getMessage()));
    }

    @DisplayName("유효한 userStuffId로 삭제 요청 성공")
    @Test
    public void userstuff_삭제_성공() throws Exception {
        // given
        UserStuffRequestDto createDto = UserStuffRequestDto.builder()
                .userId(userId)
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        // when
        mockMvc.perform(patch("/api/v1/userstuffs/"+userStuffId));

        // then
        mockMvc.perform(get("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_STUFF_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_STUFF_NOT_FOUND.getMessage()));

    }


}

