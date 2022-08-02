package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.stuff.domain.StuffType;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class StoreGroupResponseDto {

    private String stuffType;
    private String stuffTypeName;
    private Slice<StoreResponseDto> slice;

    public StoreGroupResponseDto(StuffType stuffType, Slice<StoreResponseDto> slice) {
        this.stuffType = stuffType.getEnum();
        this.stuffTypeName = stuffType.getEnumName();
        this.slice = slice;
    }
}
