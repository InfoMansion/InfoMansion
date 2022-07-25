package com.infomansion.server.domain.stuff.dto;

import com.infomansion.server.domain.category.Category;
import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffFile;
import com.infomansion.server.domain.stuff.domain.StuffType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StuffResponseDto {

    private Long id;
    private String stuffName;
    private String stuffNameKor;
    private Long price;
    private Category category;
    private StuffType stuffType;
    private String stuffGlbPath;

    public StuffResponseDto(Stuff stuff) {
        this.id = stuff.getId();
        this.stuffName = stuff.getStuffName();
        this.stuffNameKor = stuff.getStuffNameKor();
        this.price = stuff.getPrice();
        this.category = stuff.getCategory();
        this.stuffType = stuff.getStuffType();
        if(stuff.getStuffFile() != null)
            this.stuffGlbPath = stuff.getStuffFile().getStuffGlbPath();
    }
}
