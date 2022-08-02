package com.infomansion.server.domain.stuff.service.impl;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StoreGroupResponseDto;
import com.infomansion.server.domain.stuff.dto.StoreResponseDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StoreService;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreServiceImpl implements StoreService {

    private final StuffRepository stuffRepository;
    private final UserStuffRepository userStuffRepository;

    @Override
    public List<StoreGroupResponseDto> findAllStuffInStore(Integer pageSize) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Stuff> list = stuffRepository.findAllStuffInStore(userId, pageSize+1);

        List<StoreGroupResponseDto> responseDtoList = new ArrayList<>();

        StuffType stuffType = null;
        List<StoreResponseDto> stuffs = new ArrayList<>();
        for (Stuff stuff : list) {
            if(stuffType == null) stuffType = stuff.getStuffType();

            // 같은 stuffType인 경우 stuffs 목록에 responseDto 형태로 추가
            if(stuffType == stuff.getStuffType()) {
                stuffs.add(new StoreResponseDto(stuff));
            }
            // 다른 stuffType일 경우 현재까지의 stuffs를 Slice로 만들어 responseDtoList에 추가
            else {
                responseDtoList.add(entityToResponseDto(pageSize, stuffs, stuffType));

                // 현재 stuffType으로 초기화
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
        List<Long> stuffIds = userStuffRepository.findByUserId(SecurityUtil.getCurrentUserId());
        return stuffRepository.findByStuffTypeAndIdNotIn(stuffType, stuffIds, pageable);
    }

    private StoreGroupResponseDto entityToResponseDto(int pageSize, List<StoreResponseDto> stuffs, StuffType stuffType) {
        // stuffs를 가져올 때 pageSize + 1 만큼 요청했다.
        // 만약 pageSize보다 1개가 더 들어왔다면 다음 페이지가 존재하므로 hasNext = true
        // pageSize보다 작거나 같은 경우 다음 페이지가 존재하지 않기 때문에 hasNext = false
        boolean hasNext = false;
        if(stuffs.size() > pageSize) {
            hasNext = true;
            stuffs.remove(pageSize);
        }
        // 현재 페이지가 첫 페이지라는 것을 설정
        Pageable pageable = PageRequest.of(0, pageSize);
        SliceImpl<StoreResponseDto> slice = new SliceImpl<>(stuffs, pageable, hasNext);

        return new StoreGroupResponseDto(stuffType, slice);
    }
}
