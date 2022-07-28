package com.infomansion.server.domain.stuff.domain;

import com.infomansion.server.domain.base.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Stuff extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "stuff_id")
    private Long id;

    private String stuffName;
    private String stuffNameKor;
    private Long price;

    private String categories;

    @Enumerated(EnumType.STRING)
    private StuffType stuffType;

    private String geometry;
    private String materials;
    private String stuffGlbPath;

    private Boolean deleteFlag;

    @Builder
    public Stuff(Long id, String stuffName, String stuffNameKor, Long price, String categories, StuffType stuffType, String geometry, String materials, String stuffGlbPath) {
        this.id = id;
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.categories = categories;
        this.stuffType = stuffType;
        this.geometry = geometry;
        this.materials = materials;
        this.stuffGlbPath = stuffGlbPath;
        this.deleteFlag = false;
    }

    public void updateStuff(String stuffName, String stuffNameKor, Long price, String categories, String stuffType, String geometry, String materials) {
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.stuffType = StuffType.valueOf(stuffType);
        this.categories = categories;
        this.geometry = geometry;
        this.materials = materials;
    }

    /**
     * Stuff 삭제 시 실제로 데이터를 삭제하지 않고 DeleteFlag를 통해 삭제되었다고 표시
     */
    public void deleteStuff() {
        this.deleteFlag = true;
        this.setDeletedDate();
    }
}
