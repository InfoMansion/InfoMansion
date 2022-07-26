package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        String categories = "IT,GAME";
        StuffType stuffType = StuffType.STUFF;

        Stuff stuff = Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .build();

        stuffRepository.save(stuff);

        List<Stuff> stuffList = stuffRepository.findAll();
        assertThat(stuffList.get(0).getStuffName()).isEqualTo(stuffName);
        assertThat(stuffList.get(0).getStuffNameKor()).isEqualTo(stuffNameKor);
    }

    @DisplayName("Stuff 생성 시간 조회 성공")
    @Test
    public void Stuff_생성시간_조회_성공() {
        String stuffName = "laptop";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME";
        StuffType stuffType = StuffType.STUFF;

        Stuff stuff = Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .build();

        LocalDateTime createdTime = LocalDateTime.now();

        stuffRepository.save(stuff);
        List<Stuff> list = stuffRepository.findAll();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getCreatedDate()).isAfterOrEqualTo(createdTime);
    }

    @DisplayName("Stuff 수정 시간 조회 성공")
    @Test
    public void Stuff_수정시간_조회_성공() {
        // given
        String stuffName = "laptop";
        String stuffNameKor = "노트북";
        Long price = 30L;
        String categories = "IT,GAME";
        StuffType stuffType = StuffType.STUFF;

        Stuff stuff = Stuff.builder()
                .stuffName(stuffName)
                .stuffNameKor(stuffNameKor)
                .price(price)
                .categories(categories)
                .stuffType(stuffType)
                .build();
        stuff = stuffRepository.save(stuff);

        // when
        LocalDateTime createdTime = LocalDateTime.now();
        stuff.updateStuff("TV", "티비", 50L, stuff.getCategories(), "STUFF", stuff.getGeometry(), stuff.getMaterials());
        stuffRepository.flush();

        // then
        List<Stuff> list = stuffRepository.findAll();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getModifiedDate()).isAfterOrEqualTo(createdTime);
    }
}
