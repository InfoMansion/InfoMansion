package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StuffService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StuffServiceImpl implements StuffService {

    private final StuffRepository stuffRepository;

    @Transactional
    @Override
    public Long createStuff(StuffRequestDto requestDto) {
        // 하나의 String으로 받아온 categories를 분리
        List<String> categories = splitCategories(requestDto.getCategories());
        // 분리된 categories 검증
        categories.forEach(category -> validateCategory(category));

        return stuffRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    @Override
    public Long updateStuff(Long stuff_id, StuffRequestDto requestDto) {
        // 하나의 String으로 받아온 categories를 분리
        List<String> categories = splitCategories(requestDto.getCategories());
        // 분리된 categories 검증
        categories.forEach(category -> validateCategory(category));

        Stuff stuff = stuffRepository.findById(stuff_id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));
        stuff.updateStuff(requestDto.getStuffName(), requestDto.getStuffNameKor(), requestDto.getPrice(),
                requestDto.getCategories(), requestDto.getStuffType(),
                requestDto.getGeometry(), requestDto.getMaterials());
        return stuff.getId();
    }

    @Override
    public StuffResponseDto findStuffById(Long stuff_id) {
        Stuff stuff = stuffRepository.findById(stuff_id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));
        return new StuffResponseDto(stuff);
    }

    @Override
    public List<StuffResponseDto> findAllStuff() {
        List<StuffResponseDto> responseDtoList = new ArrayList<>();
        stuffRepository.findAll()
                .forEach(stuff -> responseDtoList.add(new StuffResponseDto(stuff)));
        return responseDtoList;
    }

    @Transactional
    @Override
    public void removeStuff(Long stuff_id) {
        validateStuffId(stuff_id);
        stuffRepository.deleteById(stuff_id);
    }

    private void validateStuffId(Long stuff_id) {
        if(!stuffRepository.existsById(stuff_id)) throw new CustomException(ErrorCode.STUFF_NOT_FOUND);
    }

    // String categories 분리
    private List<String> splitCategories(String categories) {
        List<String> splitCategories = Arrays.stream(categories.split(",")).collect(Collectors.toList());
        if(splitCategories.size() > 5) throw new CustomException(ErrorCode.EXCEEDED_THE_NUMBER_OF_CATEGORIES);

        return splitCategories;
    }

    // String category 검증
    private void validateCategory(String category) {
        for (Category value : Category.values()) {
            if(category.equals(value.toString())) return;
        }
        throw new CustomException(ErrorCode.NOT_VALID_CATEGORY);
    }
}
