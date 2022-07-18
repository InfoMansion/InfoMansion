package com.infomansion.server.domain.stuff.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class StuffImage {

    @Id @GeneratedValue
    @Column(name = "stuff_image_id")
    private Long id;

    private String stuffImageUrl;
    private String stuffImageName;

    @Builder
    public StuffImage(String stuffImageUrl, String stuffImageName) {
        this.stuffImageUrl = stuffImageUrl;
        this.stuffImageName = stuffImageName;
    }
}
