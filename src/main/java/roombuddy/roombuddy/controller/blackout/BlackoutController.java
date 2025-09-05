package roombuddy.roombuddy.controller.blackout;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roombuddy.roombuddy.dto.api.ApiResponseDto;
import roombuddy.roombuddy.service.blackout.BlackoutService;

/**
 *블랙 아웃 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/blackout")
public class BlackoutController {

    private final BlackoutService blackoutService;//블랙 아웃 서비스

    /**
     * [컨트롤러]
     * 특정 스터디룸에 대한 오늘의 블랙 아웃 목록 조회
     * @param id 스터디룸 아이디
     * @return PagedResponseDto<BlackoutListResponseDto>
     */
    @GetMapping("/rooms/{id}/today")
    public ResponseEntity<ApiResponseDto<?>> getTodayBlackoutsByRoom(@PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(ApiResponseDto.success(blackoutService.getTodayBlackoutsByRoom(id)));

    }

}
