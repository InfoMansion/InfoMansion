package com.infomansion.server.domain.stuff.domain;

import com.infomansion.server.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Stuff {

    @Id @GeneratedValue
    @Column(name = "stuff_id")
    private Long id;

    private String stuffName;
    private String stuffNameKor;
    private Long price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private StuffType stuffType;

    private String geometry;
    private String materials;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stuff_file_id")
    private StuffFile stuffFile;


    @Builder
    public Stuff(Long id, String stuffName, String stuffNameKor, Long price, Category category, StuffType stuffType, String geometry, String materials, StuffFile stuffFile) {
        this.id = id;
        this.stuffName = stuffName;
        this.stuffNameKor = stuffNameKor;
        this.price = price;
        this.category = category;
        this.stuffType = stuffType;
        this.geometry = geometry;
        this.materials = materials;
        this.stuffFile = stuffFile;
    }
}
