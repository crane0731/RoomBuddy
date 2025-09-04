package roombuddy.roombuddy.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 예약 생성 요청 DTO
 */
@Getter
@Setter
public class CreateReservationRequestDto {
    @NotNull(message = "시작 시간을 입력해 주세요.")
    private LocalDateTime startAt; //시작 시간
    @NotNull(message = "종료 시간을 입력해 주세요.")
    private LocalDateTime endAt; //종료 시간
    @NotNull(message = "예약기간을 입력해주세요.")
    private Long duration; //예약 기간

}
