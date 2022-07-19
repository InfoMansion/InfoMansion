package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.stuff.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StuffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

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
    public void stuff_생성() {
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

        // when
        Long stuff_id = stuffService.createStuff(requestDto);

        // then
        Optional<Stuff> findStuff = stuffRepository.findById(stuff_id);
        assertThat(findStuff.get().getStuffNameKor()).isEqualTo(stuffNameKor);

    }
}
