package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StuffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StuffServiceImplTest {

    @Autowired
    private StuffService stuffService;

    @Autowired
    private StuffRepository stuffRepository;

    @AfterEach
    public void cleanUp() {
        stuffRepository.deleteAll();
    }

    @Test
    public void stuff_생성_조회() {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME";
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

        // when
        Long stuff_id = stuffService.createStuff(requestDto);

        // then
        Optional<Stuff> findStuff = stuffRepository.findById(stuff_id);
        assertThat(findStuff.get().getStuffNameKor()).isEqualTo(stuffNameKor);
    }

    @Test
    public void stuff_수정() {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT";
        String stuffType = "OTHER";

        // DB 저장 용
        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .geometry("geometry")
                .material("materials")
                .build();
        Long stuff_id = stuffService.createStuff(requestDto);

        // Stuff 수정
        // when
        StuffRequestDto updateRequestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories("IT,GAME")
                .stuffType(stuffType)
                .geometry("geometry")
                .material("materials")
                .build();
        Long update_stuff_id = stuffService.updateStuff(stuff_id, updateRequestDto);

        // then
        Optional<Stuff> findStuff = stuffRepository.findById(stuff_id);
        assertThat(stuff_id).isEqualTo(update_stuff_id);
        assertThat(findStuff.get().getCategories()).isEqualTo("IT,GAME");
    }

    @Test
    public void stuff_전체조회() {
        // given
        for(int i = 0; i < 10; i++) {
            String stuffName = "notebook"+(i+1);
            String stuffNameKor = "노트북"+(i+1);
            Long price = 30L;
            String categories = "IT";
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
            stuffService.createStuff(requestDto);
        }

        // when
        List<StuffResponseDto> stuffList = stuffService.findAllStuff();

        // then
        assertThat(stuffList.size()).isEqualTo(10);
    }

    @Test
    public void stuff_삭제() {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT";
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
        Long stuff_id = stuffService.createStuff(requestDto);

        // when
        stuffService.removeStuff(stuff_id);

        // then
        Optional<Stuff> findStuff = stuffRepository.findById(stuff_id);
        assertThat(findStuff.isEmpty()).isEqualTo(true);
    }


}
