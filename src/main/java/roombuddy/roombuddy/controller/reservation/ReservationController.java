package roombuddy.roombuddy.controller.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import roombuddy.roombuddy.dto.api.ApiResponseDto;
import roombuddy.roombuddy.dto.reservation.CreateReservationRequestDto;
import roombuddy.roombuddy.jpaservice.reservation.JpaReservationService;
import roombuddy.roombuddy.service.reservation.ReservationService;
import roombuddy.roombuddy.utils.ErrorCheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 예약 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/reservation")
public class ReservationController {

    private final ReservationService reservationService;//예약 서비스

    private final JpaReservationService jpaReservationService;//JPA 예약 서비스

    /**
     * [컨트롤러]
     * 예약
     * @param id 스터디 룸 아이디
     * @param requestDto 생성 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("/rooms/{id}")
    public ResponseEntity<ApiResponseDto<?>> createReservation(@PathVariable("id")Long id, @Valid @RequestBody CreateReservationRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        jpaReservationService.createReservation(id,requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "예약 성공")));

    }

    /**
     * [컨트롤러]
     * 예약 취소
     * @param id 예약 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> cancelReservation(@PathVariable("id")Long id){
        jpaReservationService.cancelReservation(id);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "예약 취소 성공")));
    }


    /**
     * [서비스 로직]
     * 특정 스터디룸의 당일 예약 목록 확인
     * @param id 스터디룸 아이디
     * @return List<ReservationListResponseDto>
     */
    @GetMapping("/rooms/{id}")
    public ResponseEntity<ApiResponseDto<?>> getTodayReservation(@PathVariable("id")Long id){

        return ResponseEntity.ok(ApiResponseDto.success(jpaReservationService.getReservationsByRoomAndDate(id)));

    }

    /**
     * [컨트롤러]
     * 회원 자신의 예약 목록 확인
     * @return List<MyReservationListResponseDto>
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<?>> getMyReservation(){

        return ResponseEntity.ok(ApiResponseDto.success(jpaReservationService.getMyReservation()));

    }

}
