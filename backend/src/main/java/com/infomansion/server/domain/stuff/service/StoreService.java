package com.infomansion.server.domain.stuff.service;

import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StoreGroupResponseDto;
import com.infomansion.server.domain.stuff.dto.StoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface StoreService {

    List<StoreGroupResponseDto> findAllStuffInStore(Integer pageSize);

    Slice<StoreResponseDto> findStuffWithStuffTypeInStore(StuffType stuffType, Pageable pageable);
}
