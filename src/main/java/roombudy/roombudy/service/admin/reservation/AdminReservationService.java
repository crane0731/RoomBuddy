package roombudy.roombudy.service.admin.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombudy.roombudy.dto.api.PagedResponseDto;
import roombudy.roombudy.dto.reservation.ReservationListResponseDto;
import roombudy.roombudy.dto.reservation.SearchReservationCondDto;
import roombudy.roombudy.service.reservation.ReservationService;


/**
 * 관리자 - 예약 컨트롤러
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReservationService {

    private final ReservationService reservationService;//예약 서비스

    /**
     * [서비스 로직]
     * 관리자 - 예약 취소
     * @param reservationId 예약 아이디
     */
    @Transactional
    public void cancelReservation(Long reservationId) {
        reservationService.softDelete(reservationId);
    }


    /**
     * [서비스 로직]
     * 관리자 - 특정 스터디룸의 예약 로그 확인
     * @param roomId 스터디룸 아이디
     * @param cond 검색 조건
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    public PagedResponseDto<ReservationListResponseDto> getAllReservationsByRoom(Long roomId, SearchReservationCondDto cond,int page) {
        return reservationService.getReservationsByRoomAndCond(cond,roomId,page);
    }



    /**
     * [서비스 로직]
     * 관리자 - 현재 예약 중인 예약 목록 확인
     * @param roomId 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    public PagedResponseDto<ReservationListResponseDto> getConfirmedReservations(Long roomId,int page){

        return reservationService.getConfirmedReservations(roomId,page);

    }


}
