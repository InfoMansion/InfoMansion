package com.infomansion.server.domain.userstuff.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserStuffPurchaseRequestDto {

    @NotEmpty
    private List<Long> stuffIds = new ArrayList<>();
}
