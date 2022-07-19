package com.infomansion.server.domain.stuff.service;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffCreateRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;

import java.util.List;

public interface StuffService {

    // 관리자가 Stuff를 추가
    Stuff createStuff(StuffCreateRequestDto requestDto);

    // 관리자가 Stuff를 수정
    Stuff updateStuff(StuffCreateRequestDto requestDto);

    // 관리자가 Stuff를 단건 조회
    Stuff findStuffById(StuffRequestDto requestDto);

    // 관리자가 Stuff를 모두 조회
    List<Stuff> findAllStuff();

    // 관리자가 Stuff를 단건 삭제
    void removeStuff(StuffRequestDto requestDto);
}
