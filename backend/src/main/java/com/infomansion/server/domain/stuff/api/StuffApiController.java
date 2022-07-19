package com.infomansion.server.domain.stuff.api;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffUpdateRequestDto;
import com.infomansion.server.domain.stuff.service.StuffService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StuffApiController {

    private final StuffService stuffService;

    @PostMapping("/api/v1/auth/stuff/create")
    public ResponseEntity<CommonResponse<Long>> createStuff(@Valid @RequestBody StuffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(stuffService.createStuff(requestDto)));
    }

//    @GetMapping("/api/v1/stuff")
//    public ResponseEntity<CommonResponse<List<Stuff>>> findAllStuff() {
//        return null;
//    }
//
//    @GetMapping("/api/v1/stuff/{stuff_id}")
//    public ResponseEntity<CommonResponse<Stuff>> findStuffById(@PathVariable Long stuff_id) {
//        return null;
//    }
//
//    @PutMapping("/api/v1/stuff/{stuff_id}")
//    public ResponseEntity<CommonResponse<Integer>> updateStuff(@PathVariable Long stuff_id, @Valid @RequestBody StuffRequestDto requestDto) {
//        return null;
//    }
//
//    @DeleteMapping("/api/v1/stuff/{stuff_id}")
//    public ResponseEntity<CommonResponse<Integer>> removeStuff(@Valid @PathVariable Long stuff_id) {
//        return null;
//    }

}
