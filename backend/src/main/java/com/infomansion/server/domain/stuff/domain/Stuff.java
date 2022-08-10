package com.infomansion.server.domain.stuff.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor
@Where(clause = "delete_flag = false")
@Entity
public class Stuff extends BaseTimeEntityAtSoftDelete {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stuff_id")
    private Long id;

    private String stuffName;
    private String stuffNameKor;
    private Long price;

    private String categories;

    @Enumerated(EnumType.STRING)
    private StuffType stuffType;

    private String geometry;
    private String material;
    private String stuffGlbPath;
    private String backgroundByWall;

    private Boolean deleteFlag;

    @Builder
    public Stuff(Long id, String stuffName, String stuffNameKor, Long price, String categories, StuffType stuffType, String geometry, String material, String stuffGlbPath, String backgroundByWall) {
        this.id = id;
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.categories = categories;
        this.stuffType = stuffType;
        this.geometry = geometry;
        this.material = material;
        this.stuffGlbPath = stuffGlbPath;
        this.backgroundByWall = (stuffType.getEnum().equals("WALL"))? backgroundByWall : "notwall";
        this.deleteFlag = false;
    }

    public List<String> getCategoryList() {
        return Arrays.stream(categories.split(",")).collect(Collectors.toList());
    }

    public void updateStuff(String stuffName, String stuffNameKor, Long price, String categories, String stuffType, String geometry, String material) {
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.stuffType = StuffType.valueOf(stuffType);
        this.categories = categories;
        this.geometry = geometry;
        this.material = material;
    }

    /**
     * Stuff 삭제 시 실제로 데이터를 삭제하지 않고 DeleteFlag를 통해 삭제되었다고 표시
     */
    public void deleteStuff() {
        this.deleteFlag = true;
        this.setDeletedDate();
    }
}
