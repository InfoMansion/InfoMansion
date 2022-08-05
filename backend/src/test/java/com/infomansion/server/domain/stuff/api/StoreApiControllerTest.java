package com.infomansion.server.domain.stuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.dto.UserStuffSaveRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.security.WithCustomUserDetails;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class StoreApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StuffRepository stuffRepository;

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

        stuffIds = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName("laptop"+(i+1))
                    .stuffNameKor("노트북"+(i+1))
                    .price(Long.valueOf((i+1)*5))
                    .categories("IT,GAME")
                    .stuffType("OTHER")
                    .geometry("geometry")
                    .material("materials").build();

            if(i == 0) stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
            else stuffRepository.save(requestDto.toEntity());
        }

        for(int i = 0; i < 8; i++) {
            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName("plant"+(i+1))
                    .stuffNameKor("화분"+(i+1))
                    .price(Long.valueOf((i+1)*2))
                    .categories("NATURE")
                    .stuffType("PLANT")
                    .geometry("geometry")
                    .material("materials").build();

            if(i == 0) stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
            else stuffRepository.save(requestDto.toEntity());
        }

        for(int i = 0; i < 3; i++) {
            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName("yoga"+(i+1))
                    .stuffNameKor("요가매트"+(i+1))
                    .price(Long.valueOf((i+1)*3))
                    .categories("SPORTS")
                    .stuffType("WORKOUT")
                    .geometry("geometry")
                    .material("materials").build();

            if(i == 0) stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
            else stuffRepository.save(requestDto.toEntity());
        }
    }

    @AfterEach
    public void cleanUp() {
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("사용자가 가지고 있는 Stuff를 제외한 모든 Stuff 조회 성공")
    @WithCustomUserDetails
    @Test
    public void find_stuff_in_store_성공() throws Exception {
        for (Long stuffId : stuffIds) {
            UserStuffSaveRequestDto request = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffId).build();
            String requestJson = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/userstuffs/list")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/v1/stores?pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(3))
                .andExpect(jsonPath("$.data[0].slice.numberOfElements").value(4))
                .andExpect(jsonPath("$.data[1].slice.numberOfElements").value(5))
                .andExpect(jsonPath("$.data[2].slice.numberOfElements").value(2));
    }

    @DisplayName("유효한 가구타입 및 페이지로 구매할 수 있는 Stuff 조회 시 성공")
    @WithCustomUserDetails
    @Test
    public void find_stuff_with_stuffType_in_store_성공() throws Exception {
        for (Long stuffId : stuffIds) {
            UserStuffSaveRequestDto request = UserStuffSaveRequestDto.builder()
                    .stuffId(stuffId).build();
            String requestJson = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/userstuffs/list")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        int page = 1;
        int size = 3;
        mockMvc.perform(get("/api/v1/stores/PLANT?page="+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.number").value(page))
                .andExpect(jsonPath("$.data.size").value(size))
                .andExpect(jsonPath("$.data.numberOfElements").value(size))
                .andDo(print());
    }

    @DisplayName("존재하지 않는 가구타입으로 구매할 수 있는 Stuff 조회 시 실패")
    @WithCustomUserDetails
    @Test
    public void find_stuff_with_stuffType_in_store_실패_1() throws Exception {
        int page = 0;
        int size = 3;
        mockMvc.perform(get("/api/v1/stores/KEYBOARD?page="+page+"&size="+size))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유효하지 않은 페이지로 구매할 수 있는 Stuff 조회 시 실패")
    @WithCustomUserDetails
    @Test
    public void find_stuff_with_stuffType_in_store_실패_2() throws Exception {
        int page = 1000;
        int size = 3;
        mockMvc.perform(get("/api/v1/stores/PLANT?page="+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.number").value(page))
                .andExpect(jsonPath("$.data.size").value(size))
                .andExpect(jsonPath("$.data.numberOfElements").value(0));
    }
}
