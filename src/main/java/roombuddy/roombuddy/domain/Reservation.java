package roombuddy.roombuddy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.ReservationStatus;

import java.time.LocalDateTime;

/**
 * 예약
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    private Long reservationId;//PK

    private Long memberId; //회원 아이디

    private Long roomId;//스터디룸 아이디

    private ReservationStatus status;//예약 상태

    private LocalDateTime startAt; //시작 시간

    private LocalDateTime endAt; //종료 시간

    private Long duration; //예약 기간

    private ActiveStatus activeStatus; //활성화 상태

    private LocalDateTime createdAt;//생성일

    private LocalDateTime updatedAt;//수정일


    /**
     * [생성 메서드]
     * @param memberId 회원 아이디
     * @param roomId 스터디룸 아이디
     * @param startAt 예약 시작 시간
     * @param endAt 예약 종료 시간
     * @param duration 예약 기간
     * @return Reservation
     */
    public static Reservation create(Long memberId, Long roomId,LocalDateTime startAt, LocalDateTime endAt,Long duration) {
        Reservation reservation = new Reservation();
        reservation.memberId = memberId;
        reservation.roomId = roomId;
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.startAt = startAt;
        reservation.endAt = endAt;
        reservation.duration = duration;
        reservation.setActiveStatus(ActiveStatus.ACTIVE);
        return reservation;
    }

}




