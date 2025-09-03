package roombudy.roombudy.controller.admin.blackout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import roombudy.roombudy.dto.api.ApiResponseDto;
import roombudy.roombudy.dto.blackout.CreateBlackoutRequestDto;
import roombudy.roombudy.service.blackout.BlackoutService;
import roombudy.roombudy.utils.ErrorCheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 관리자 - 블랙 아웃 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/admin/blackout")
public class AdminBlackoutController {

    private final BlackoutService blackoutService;//블랙 아웃 서비스



    /**
     * [컨트롤러]
     * 관리자 - 블랙 아웃 등록
     * @param requestDto 생성 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponseDto<?>> createBlackout(@Valid @RequestBody CreateBlackoutRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        blackoutService.createBlackout(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "블랙 아웃 생성 성공")));

    }



    /**
     * [컨트롤러]
     * 관리자 - 블랙 아웃 제거
     * @param id 블랙 아웃 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteBlackout(@PathVariable("id") Long id) {
        blackoutService.deleteBlackout(id);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "블랙 아웃 제거 성공")));

    }


    /**
     * [컨트롤러]
     * 관리자 - 현재 진행중인& 예정인 블랙 아웃 목록 조회
     * @param page 페이지 번호
     * @return PagedResponseDto<BlackoutListResponseDto>
     */
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<?>> getBlackoutsByRoom(@RequestParam(value = "page", defaultValue = "0") int page
                                                                ) {
        return ResponseEntity.ok(ApiResponseDto.success(blackoutService.getActiveBlackoutsByRoom(page)));

    }

}
