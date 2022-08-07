package com.infomansion.server.domain.stuff.api;

import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StoreGroupResponseDto;
import com.infomansion.server.domain.stuff.dto.StoreResponseDto;
import com.infomansion.server.domain.stuff.service.StoreService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.util.validation.ValidEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;

    @GetMapping("/api/v1/stores")
    public ResponseEntity<CommonResponse<List<StoreGroupResponseDto>>> findAllStuffInStore(@RequestParam Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(storeService.findAllStuffInStore(pageSize)));
    }

    @GetMapping("/api/v1/stores/{stuffType}")
    public ResponseEntity<CommonResponse<Slice<StoreResponseDto>>> findStuffWithTypeInStore(@ValidEnum(enumClass = StuffType.class) @PathVariable StuffType stuffType, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(storeService.findStuffWithStuffTypeInStore(stuffType, pageable)));
    }

    @GetMapping("/api/v1/stores/latest")
    public ResponseEntity<? extends BasicResponse> findTheLatestStuff() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(storeService.findTheLatestStuff()));
    }

}
