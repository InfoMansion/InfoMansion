package com.infomansion.server.domain.category.api;

import com.infomansion.server.domain.category.domain.CategoryMapperValue;
import com.infomansion.server.domain.category.service.CategoryService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("/api/v1/categories")
    public ResponseEntity<CommonResponse<List<CategoryMapperValue>>> findAllCategory() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(categoryService.findAllCategory()));
    }
}
