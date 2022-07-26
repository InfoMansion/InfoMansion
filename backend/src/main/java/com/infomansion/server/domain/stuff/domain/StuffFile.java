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
public class StuffFile {

    @Id @GeneratedValue
    @Column(name = "stuff_file_id")
    private Long id;

    private String stuffGlbPath;

    @Builder
    public StuffFile(String stuffGlbPath) {
        this.stuffGlbPath = stuffGlbPath;
    }
}
