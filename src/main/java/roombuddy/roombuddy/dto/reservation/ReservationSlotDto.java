package roombuddy.roombuddy.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 예약 슬롯 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class ReservationSlotDto {

    private LocalDateTime startAt; //예약 시작 시간
    private LocalDateTime endAt; //예약 종료 시간

}
