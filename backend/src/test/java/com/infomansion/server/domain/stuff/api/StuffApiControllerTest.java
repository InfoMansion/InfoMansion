package com.infomansion.server.domain.stuff.api;

import com.infomansion.server.domain.stuff.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StuffApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StuffRepository stuffRepository;

    @AfterEach
    public void cleanUp() {
        stuffRepository.deleteAll();
    }

    @DisplayName("Stuff 생성 성공")
    @Test
    public void stuff_생성_성공() {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
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

        String createUrl = "http://localhost:" + port + "/api/v1/stuff/create";

        //when
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(createUrl, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isInstanceOf(BasicResponse.class);

        CommonResponse res = (CommonResponse) responseEntity.getBody();

        Optional<Stuff> stuff = stuffRepository.findById(Long.valueOf((Integer)res.getData()));

        assertThat(stuff.get().getStuffName()).isEqualTo(stuffName);
        assertThat(stuff.get().getStuffNameKor()).isEqualTo(stuffNameKor);
        assertThat(stuff.get().getPrice()).isEqualTo(price);
        assertThat(stuff.get().getCategory()).isEqualTo(Category.valueOf(category));
        assertThat(stuff.get().getStuffType()).isEqualTo(StuffType.valueOf(stuffType));
    }

    @DisplayName("Stuff 생성 실패")
    @Test
    public void stuff_생성_실패() {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String category = "BOOK";
        String stuffType = "NONE";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .category(category)
                .stuffType(stuffType)
                .build();

        String createUrl = "http://localhost:" + port + "/api/v1/auth/stuff/create";

        //when
        ResponseEntity<? extends BasicResponse> responseEntity = restTemplate.postForEntity(createUrl, requestDto, CommonResponse.class);
        System.out.println("responseEntity = " + responseEntity);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
