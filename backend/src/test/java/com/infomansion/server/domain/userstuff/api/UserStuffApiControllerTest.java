package com.infomansion.server.domain.userstuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.dto.UserStuffEditRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffModifyRequestDto;
import com.infomansion.server.domain.userstuff.dto.UserStuffSaveRequestDto;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.exception.ErrorCode;
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
import org.springframework.transaction.annotation.Transactional;

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
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private List<Long> stuffIds;
    private User user;

    @BeforeEach
    public void setUp() {
        // user 생성
        String email = "infomansion@test.com";
        String password = "testPassword1$";
        String tel = "01012345678";
        String username = "infomansion";
        String uCategories = "IT,COOK";

        user = userRepository.save(User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .username(username)
                .categories(uCategories)
                .build());

        userId = user.getId();

        // stuff 생성
        stuffIds = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            String stuffName = "notebook"+(i+1);
            String stuffNameKor = "노트북"+(i+1);
            Long price = 30L;
            String categories = "IT,GAME,DAILY";
            String stuffType = "OTHER";

            StuffRequestDto requestDto = StuffRequestDto.builder()
                    .stuffName(stuffName)
                    .stuffNameKor(stuffNameKor)
                    .price(price)
                    .categories(categories)
                    .stuffType(stuffType)
                    .geometry("geometry")
                    .material("materials")
                    .build();

            stuffIds.add(stuffRepository.save(requestDto.toEntity()).getId());
        }
    }

    @AfterEach
    public void reset() {
        postRepository.deleteAll();
        userStuffRepository.deleteAll();
        stuffRepository.deleteAll();
        userRepository.deleteAll();
    }


    @DisplayName("유효하지 않은 stuff_id로 UserStuff 생성 실패")
    @WithCustomUserDetails
    @Test
    public void userstuff_생성_실패() throws Exception {
        // given
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)+100L).build();

        String createDtoJson = objectMapper.writeValueAsString(createDto);

        // when, then
        mockMvc.perform(post("/api/v1/userstuffs/list")
                        .content(createDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.STUFF_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.STUFF_NOT_FOUND.getCode()));
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

    @DisplayName("alias, category 둘 다 미입력 시 실패")
    @WithCustomUserDetails
    @Test
    public void userstuff_alias_and_category_수정_실패() throws Exception {
        // given
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        List<UserStuffEditRequestDto> requestDto = new ArrayList<>();
        requestDto.add(includeDto);

        String includeDtoJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(put("/api/v1/userstuffs/edit")
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
    @WithCustomUserDetails
    @Test
    public void userstuff_category_수정_실패() throws Exception {
        // given
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        List<UserStuffEditRequestDto> requestDto = new ArrayList<>();
        requestDto.add(includeDto);

        String includeDtoJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(put("/api/v1/userstuffs/edit")
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

    @DisplayName("유효하지 않은 userStuffId로 삭제 요청 시 실패")
    @WithCustomUserDetails
    @Test
    public void userstuff_삭제_실패() throws Exception {
        // given
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        // when, then
        mockMvc.perform(patch("/api/v1/userstuffs/"+(userStuffId+999999)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_STUFF_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_STUFF_NOT_FOUND.getMessage()));
    }

    @DisplayName("유효한 userStuffId로 삭제 요청 성공")
    @WithCustomUserDetails
    @Transactional
    @Test
    public void userstuff_삭제_성공() throws Exception {

        String stuffName = "postbox";
        String stuffNameKor = "저장소";
        Long price = 30L;
        String sCategories = "POSTBOX";
        String stuffType = "POSTBOX";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(sCategories)
                .stuffType(stuffType)
                .geometry("geometry")
                .material("materials")
                .build();

        //Postbox Stuff 생성
        Stuff stuff = stuffRepository.save(requestDto.toEntity());
        Long postboxStuffId = stuff.getId();

        //Postbox UserStuff 생성
        UserStuffSaveRequestDto postboxSaveRequestDto = UserStuffSaveRequestDto.builder()
                .stuffId(postboxStuffId).build();

        User user = userRepository.findById(userId).get();
        Long postboxusId = userStuffService.saveUserStuff(postboxSaveRequestDto);

        String category = "POSTBOX";
        String alias = "POST저장소";

        UserStuffEditRequestDto postboxIncludeRequestDto = UserStuffEditRequestDto.builder()
                .userStuffId(postboxusId).alias(alias).category(category)
                .posX(1.1).posY(0.0).posZ(1.0).rotX(0.0).rotY(0.1).rotZ(0.1).build();
        List<UserStuffEditRequestDto> placed = new ArrayList<>();
        placed.add(postboxIncludeRequestDto);

        // given
        UserStuffSaveRequestDto createDto = UserStuffSaveRequestDto.builder()
                .stuffId(stuffIds.get(0)).build();
        Long userStuffId = userStuffService.saveUserStuff(createDto);

        UserStuffEditRequestDto includeDto = UserStuffEditRequestDto.builder()
                .userStuffId(userStuffId).alias("Java 정리").category("IT")
                .posX(0.2).posY(0.3).posZ(3.1)
                .rotX(1.5).rotY(0.0).rotZ(0.9)
                .build();
        placed.add(includeDto);
        // postbox와 stuff 1번 배치완료
        userStuffService.editUserStuff(placed);

        UserStuff userStuff = userStuffRepository.findById(userStuffId).get();

        System.out.println(userStuff);
        String title = "EffectiveJava자바 infomansion";
        String content = "infomansion Java 파이팅!";
        PostCreateRequestDto postCreateRequestDto = PostCreateRequestDto.builder()
                        .userStuffId(userStuffId).title(title).content(content).images(new ArrayList<>()).build();

        Long postId = postService.createPost(postCreateRequestDto);

        // when
        mockMvc.perform(patch("/api/v1/userstuffs/"+userStuffId));

        // then
        mockMvc.perform(get("/api/v1/userstuffs/"+userStuffId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_STUFF_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_STUFF_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/"+postboxusId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(postId));

    }


}

