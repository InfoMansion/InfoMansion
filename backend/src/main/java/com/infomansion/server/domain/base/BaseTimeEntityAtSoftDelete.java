package com.infomansion.server.domain.base;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntityAtSoftDelete extends BaseTimeEntity {

    private LocalDateTime deletedDate;

    public void setDeletedDate() {
        deletedDate = LocalDateTime.now();
    }
}
