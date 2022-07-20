package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class StuffRepositoryTest {

    @Autowired
    private StuffRepository stuffRepository;

    @DisplayName("Stuff 생성 및 조회 성공")
    @Test
    public void Stuff_생성_및_조회() {
        String stuffName = "laptop";
        String stuffNameKor = "노트북";
        Long price = 30L;
        Category category = Category.IT;
        StuffType stuffType = StuffType.STUFF;

        Stuff stuff = Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .category(category)
                .stuffType(stuffType)
                .build();

        stuffRepository.save(stuff);

        List<Stuff> stuffList = stuffRepository.findAll();
        assertThat(stuffList.get(0).getStuffName()).isEqualTo(stuffName);
        assertThat(stuffList.get(0).getStuffNameKor()).isEqualTo(stuffNameKor);
    }
}
