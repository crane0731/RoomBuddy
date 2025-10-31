package roombuddy.roombuddy.dto.reservation;

import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.jpadomain.Reservation;
import roombuddy.roombuddy.utils.DateFormatUtil;

/**
 * 회원 자신의 예약 목록 응답 DTO
 */
@Getter
@Setter
public class MyReservationListResponseDto {

    private Long reservationId ; //예약 아이디

    private String startAt; //예약 시작 시간
    private String endAt; //예약 종료 시간
    private Long duration;//예약 기간
    private String status;//예약 상태

    private Long roomId;//스터디룸 아이디
    private String name;//스터디룸 이름

    private String createdAt;//생성일

    /**
     * [생성 메서드]
     * @param reservation 예약
     * @return MyReservationListResponseDto
     */
    public static MyReservationListResponseDto create(Reservation reservation) {
        MyReservationListResponseDto dto = new MyReservationListResponseDto();
        dto.setReservationId(reservation.getId());

        dto.setStartAt(DateFormatUtil.DateFormat(reservation.getStartAt()));
        dto.setEndAt(DateFormatUtil.DateFormat(reservation.getEndAt()));
        dto.setDuration(reservation.getDuration());
        dto.setStatus(reservation.getStatus().name());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setName(reservation.getRoom().getName());
        dto.setCreatedAt(DateFormatUtil.DateFormat(reservation.getCreatedAt()));
        return dto;
    }

}
