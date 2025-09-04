package roombuddy.roombuddy.dto.blackout;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 블랙 아웃 DTO
 */
@Getter
@Setter
public class BlackoutDto {

    private LocalDateTime startAt; //블랙 아웃 시작 시간
    private LocalDateTime endAt; //블랙 아웃 종료 시간

}
