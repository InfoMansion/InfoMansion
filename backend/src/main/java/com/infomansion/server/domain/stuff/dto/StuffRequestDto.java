package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.stuff.domain.Stuff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class StuffRequestDto {

    @NotBlank
    private Long id;

    public StuffRequestDto(Long id) {
        this.id = id;
    }

    public Stuff toEntity() {
        return Stuff.builder()
                .id(id)
                .build();
    }
}
