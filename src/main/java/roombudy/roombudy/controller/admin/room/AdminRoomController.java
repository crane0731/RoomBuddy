package roombudy.roombudy.controller.admin.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import roombudy.roombudy.dto.api.ApiResponseDto;
import roombudy.roombudy.dto.room.CreateRoomRequestDto;
import roombudy.roombudy.dto.room.UpdateRoomRequestDto;
import roombudy.roombudy.service.admin.room.AdminRoomService;
import roombudy.roombudy.utils.ErrorCheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 관리자 - 스터디룸 관리 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/admin/rooms")
public class AdminRoomController {

    private final AdminRoomService adminRoomService;//관리자 스터디룸 서비스

    /**
     * [컨트롤러]
     * 스터디룸 생성
     * @param requestDto 생성 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponseDto<?>> createRoom(@Valid @RequestBody CreateRoomRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        adminRoomService.createRoom(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "스터디룸 생성 성공")));

    }


    /**
     * [컨트롤러]
     * 스터디룸 삭제
     * @param id 룸 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteRoom(@PathVariable("id") Long id) {
        adminRoomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "스터디룸 삭제 성공")));

    }

    /**
     * [컨트롤러]
     * 스터디룸 정보 수정
     * @param id 룸 아이디
     * @param requestDto 수정 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> updateRoom(@PathVariable("id") Long id, @Valid @RequestBody UpdateRoomRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        adminRoomService.updateRoom(id,requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "스터디룸 수정 성공")));

    }


}
