package com.infomansion.server.domain.stuff.service;

import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;

import java.util.List;

public interface StuffService {

    // 관리자가 Stuff를 추가
    Long createStuff(StuffRequestDto requestDto);

    // 관리자가 Stuff를 수정
    Long updateStuff(Long stuff_id, StuffRequestDto requestDto);

    // 관리자가 Stuff를 단건 조회
    StuffResponseDto findStuffById(Long stuff_id);

    // 관리자가 Stuff를 모두 조회
    List<StuffResponseDto> findAllStuff();

    // 관리자가 Stuff를 단건 삭제
    void removeStuff(Long stuff_id);
}
