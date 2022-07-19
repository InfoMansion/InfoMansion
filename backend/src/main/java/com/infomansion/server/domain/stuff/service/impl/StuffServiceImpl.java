package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffCreateRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StuffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class StuffServiceImpl implements StuffService {

    private final StuffRepository stuffRepository;


    @Override
    public Stuff createStuff(StuffCreateRequestDto requestDto) {
        return stuffRepository.save(requestDto.toEntity());
    }

    @Override
    public Stuff updateStuff(StuffCreateRequestDto requestDto) {
        return stuffRepository.save(requestDto.toEntity());
    }

    @Override
    public Stuff findStuffById(StuffRequestDto requestDto) {
        return stuffRepository.findById(requestDto.getId()).get();
    }

    @Override
    public List<Stuff> findAllStuff() {
        return stuffRepository.findAll();
    }

    @Override
    public void removeStuff(StuffRequestDto requestDto) {
        stuffRepository.deleteById(requestDto.getId());
    }
}
