package roombuddy.roombuddy.dto.reservation;

import lombok.Getter;
import lombok.Setter;

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

}
