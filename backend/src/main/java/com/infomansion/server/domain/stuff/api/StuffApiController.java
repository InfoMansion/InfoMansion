package com.infomansion.server.domain.stuff.api;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.dto.StuffCreateRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffRequestDto;
import com.infomansion.server.domain.stuff.dto.StuffUpdateRequestDto;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.stuff.service.StuffService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StuffApiController {

    private final StuffService stuffService;

    @PostMapping("/api/v1/stuff/create")
    public ResponseEntity<CommonResponse<Stuff>> createStuff(@Valid @RequestBody StuffCreateRequestDto requestDto) {
        return null;
    }

    @GetMapping("/api/v1/stuff")
    public ResponseEntity<CommonResponse<List<Stuff>>> findAllStuff() {
        return null;
    }

    @GetMapping("/api/v1/stuff")
    public ResponseEntity<CommonResponse<Stuff>> findStuffById(@Valid @RequestParam StuffRequestDto requestDto) {
        return null;
    }

    @PutMapping("/api/v1/stuff")
    public ResponseEntity<CommonResponse<Integer>> updateStuff(@Valid @RequestBody StuffUpdateRequestDto requestDto) {
        return null;
    }

    @DeleteMapping("/api/v1/stuff")
    public ResponseEntity<CommonResponse<Integer>> removeStuff(@Valid @RequestParam StuffRequestDto requestDto) {
        return null;
    }

}
