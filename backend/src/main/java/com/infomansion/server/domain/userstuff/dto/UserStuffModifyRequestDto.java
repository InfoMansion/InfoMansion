package com.infomansion.server.domain.userstuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class UserStuffModifyRequestDto {

    @NotNull
    private Long id;

    private String category;
    private String alias;

    @Builder
    public UserStuffModifyRequestDto(Long id, String category, String alias) {
        this.id = id;
        this.category = category;
        this.alias = alias;
    }

    public void isValidCategory() {
        if(category == null) return;
        String upperCategory = category.toUpperCase();
        for (Category c : Category.values()) {
            if(upperCategory.equals(c.toString())) {
                category = c.toString();
                return;
            }
        }
        throw new CustomException(ErrorCode.NOT_VALID_CATEGORY);
    }
}
