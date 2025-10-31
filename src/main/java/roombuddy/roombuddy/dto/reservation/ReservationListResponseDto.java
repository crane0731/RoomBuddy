package roombuddy.roombuddy.dto.reservation;

import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.jpadomain.Reservation;
import roombuddy.roombuddy.utils.DateFormatUtil;

/**
 * 예약 목록 응답 DTO
 */
@Getter
@Setter
public class ReservationListResponseDto {

    private Long reservationId ; //예약 아이디

    private Long memberId; //회원 아이디
    private String name;//회원 이름
    private String email;//이메일

    private String startAt; //예약 시작 시간
    private String endAt; //예약 종료 시간
    private Long duration;//예약 기간
    private String status; //예약 상태

    private String createdAt;//생성일

    /**
     * [생성 메서드]
     * @param reservation 예약
     * @return ReservationListResponseDto
     */
    public static ReservationListResponseDto create(Reservation reservation) {
        ReservationListResponseDto dto = new ReservationListResponseDto();
        dto.setReservationId(reservation.getId());
        dto.setMemberId(reservation.getMember().getId());
        dto.setName(reservation.getMember().getName());
        dto.setEmail(reservation.getMember().getEmail());

        dto.setStartAt(DateFormatUtil.DateFormat(reservation.getStartAt()));
        dto.setEndAt(DateFormatUtil.DateFormat(reservation.getEndAt()));
        dto.setDuration(reservation.getDuration());
        dto.setStatus(reservation.getStatus().name());
        dto.setCreatedAt(DateFormatUtil.DateFormat(reservation.getCreatedAt()));
        return dto;
    }

}
