package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
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
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class StuffServiceImpl implements StuffService {

    private final StuffRepository stuffRepository;

    @Override
    public Long createStuff(StuffRequestDto requestDto) {
        return stuffRepository.save(requestDto.toEntity()).getId();
    }

    @Override
    public Long updateStuff(Long stuff_id, StuffRequestDto requestDto) {
        stuffRepository.findById(stuff_id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUFF_NOT_FOUND));
        Stuff newStuff = Stuff.builder()
                .id(stuff_id)
                .stuffName(requestDto.getStuffName())
                .stuffNameKor(requestDto.getStuffNameKor())
                .price(requestDto.getPrice())
                .stuffType(StuffType.valueOf(requestDto.getStuffType()))
                .category(Category.valueOf(requestDto.getCategory()))
                .build();
        return stuffRepository.save(newStuff).getId();
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

    @Override
    public void removeStuff(Long stuff_id) {
        validationStuffId(stuff_id);
        stuffRepository.deleteById(stuff_id);
    }

    private void validationStuffId(Long stuff_id) {
        if(!stuffRepository.existsById(stuff_id)) throw new CustomException(ErrorCode.STUFF_NOT_FOUND);
    }
}
