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

import static com.infomansion.server.domain.category.util.CategoryUtil.validateCategories;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StuffServiceImpl implements StuffService {

    private final StuffRepository stuffRepository;

    @Transactional
    @Override
    public Long createStuff(StuffRequestDto requestDto) {
        validateCategories(requestDto.getCategories());
        return stuffRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    @Override
    public Long updateStuff(Long stuff_id, StuffRequestDto requestDto) {
        validateCategories(requestDto.getCategories());

        Stuff stuff = stuffRepository.findById(stuff_id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));
        stuff.updateStuff(requestDto.getStuffName(), requestDto.getStuffNameKor(), requestDto.getPrice(),
                requestDto.getCategories(), requestDto.getStuffType(),
                requestDto.getGeometry(), requestDto.getMaterial());
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
        Stuff stuff = stuffRepository.findById(stuff_id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));
        stuff.deleteStuff();
    }
}
