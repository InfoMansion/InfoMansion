package com.infomansion.server.domain.stuff.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Stuff {

    @Id @GeneratedValue
    @Column(name = "stuff_id")
    private Long id;

    private String stuffName;
    private Long price;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int stuffSize;

    @Enumerated(EnumType.STRING)
    private StuffType stuffType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stuff_image_id")
    private StuffImage stuffImage;


    @Builder
    public Stuff(String stuffName, Long price, Category category, int stuffSize, StuffType stuffType, StuffImage stuffImage) {
        this.stuffName = stuffName;
        this.price = price;
        this.category = category;
        this.stuffSize = stuffSize;
        this.stuffType = stuffType;
        this.stuffImage = stuffImage;
    }
}
