package com.infomansion.server.global;

import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationTest {


    @Test
    public void enum_유효성검사() {
        // given
        String stuffName = "notebook";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String category = "BOOK";
        String stuffType = "RADIO";

        StuffRequestDto requestDto = StuffRequestDto.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .category(category)
                .stuffType(stuffType)
                .build();

        Set<ConstraintViolation<StuffRequestDto>> validate = Validation.buildDefaultValidatorFactory()
                .getValidator().validate(requestDto);

        assertThat(validate.size()).isEqualTo(2);
        assertThat(validate).extracting("message").containsOnly("유효하지 않은 값입니다.");
    }
}
