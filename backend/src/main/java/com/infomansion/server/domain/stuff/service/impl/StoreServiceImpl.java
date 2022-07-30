package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StoreGroupResponseDto;
import com.infomansion.server.domain.stuff.dto.StoreResponseDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StoreService;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreServiceImpl implements StoreService {

    private final StuffRepository stuffRepository;

    @Override
    public List<StoreGroupResponseDto> findAllStuffInStore(Integer pageSize) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Stuff> list = stuffRepository.findAllStuffInStore(userId, pageSize+1);

        List<StoreGroupResponseDto> responseDtoList = new ArrayList<>();

        StuffType stuffType = null;
        List<StoreResponseDto> stuffs = new ArrayList<>();

        for (Stuff stuff : list) {
            if(stuffType == null) {
                stuffType = stuff.getStuffType();
            }

            if(stuffType == stuff.getStuffType()) {
                stuffs.add(new StoreResponseDto(stuff));
            } else {
                responseDtoList.add(entityToResponseDto(pageSize, stuffs, stuffType));

                stuffType = stuff.getStuffType();
                stuffs = new ArrayList<>();
                stuffs.add(new StoreResponseDto(stuff));
            }
        }
        responseDtoList.add(entityToResponseDto(pageSize, stuffs, stuffType));

        return responseDtoList;
    }

    @Override
    public Slice<StoreResponseDto> findStuffWithStuffTypeInStore(StuffType stuffType, Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<StoreResponseDto> content = new ArrayList<>();

        stuffRepository.findStuffWithStuffTypeInStore(userId, stuffType.getEnum(), pageable.getOffset(), pageable.getPageSize())
                .forEach(stuff -> content.add(new StoreResponseDto(stuff)));

        boolean hasNext = false;
        if(content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private StoreGroupResponseDto entityToResponseDto(int pageSize, List<StoreResponseDto> stuffs, StuffType stuffType) {
        boolean hasNext = false;
        if(stuffs.size() > pageSize) {
            hasNext = true;
            stuffs.remove(pageSize);
        }
        Pageable pageable = Pageable.ofSize(pageSize).first();
        SliceImpl<StoreResponseDto> slice = new SliceImpl<>(stuffs, pageable, hasNext);

        return new StoreGroupResponseDto(stuffType, slice);
    }
}
