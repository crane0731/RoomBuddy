package roombudy.roombudy.controller.admin.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roombudy.roombudy.dto.api.ApiResponseDto;
import roombudy.roombudy.dto.reservation.SearchReservationCondDto;
import roombudy.roombudy.enums.CreatedType;
import roombudy.roombudy.enums.ReservationStatus;
import roombudy.roombudy.service.admin.reservation.AdminReservationService;

import java.time.LocalDate;
import java.util.Map;

/**
 * 관리자 - 예약 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/admin/reservation")
public class AdminReservationController {

    private final AdminReservationService adminReservationService; //관리자 예약 서비스

    /**
     * [컨트롤러]
     * 관리자 - 예약 취소
     * @param id 예약 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> cancelReservation(@PathVariable("id")Long id){
        adminReservationService.cancelReservation(id);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "예약 취소 성공")));
    }


    /**
     * [컨트롤러]
     * 관리자 - 특정 스터디룸의 예약 목록 확인
     * @param id 스터디룸 아이디
     * @param memberEmail 회원 이메일
     * @param status 예약 상태
     * @param date 날짜
     * @param created 생성일 기준
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    @GetMapping("/rooms/{id}")
    public ResponseEntity<ApiResponseDto<?>> getAllReservationsByRoomAndCond(@PathVariable("id")Long id,
                                                                             @RequestParam(value = "memberEmail",required = false) String memberEmail,
                                                                             @RequestParam(value = "status",required = false)ReservationStatus status,
                                                                             @RequestParam(value = "date",required = false) LocalDate date,
                                                                             @RequestParam(value = "created",required = false) CreatedType created,
                                                                             @RequestParam(value = "page",defaultValue = "0") int page
                                                                             ){

        return ResponseEntity.ok(ApiResponseDto.success(adminReservationService.getAllReservationsByRoom(id, SearchReservationCondDto.create(memberEmail, status, date, created), page)));
    }



    /**
     * [컨트롤러]
     * 관리자 - 현재 예약중인 예약 목록 확인
     * @param id 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<?>> getAllReservationsByRoomAndCond(@RequestParam(value = "id",required = false)Long id,
                                                                             @RequestParam(value = "page",defaultValue = "0") int page
    ){

        return ResponseEntity.ok(ApiResponseDto.success(adminReservationService.getConfirmedReservations(id,page)));
    }

}
