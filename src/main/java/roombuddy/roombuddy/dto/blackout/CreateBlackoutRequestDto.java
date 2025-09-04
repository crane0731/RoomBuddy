package roombuddy.roombuddy.dto.blackout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.enums.Scope;

import java.time.LocalDateTime;

/**
 * 블랙 아웃 생성 요청 DTO
 */
@Getter
@Setter
public class CreateBlackoutRequestDto {



    @NotNull(message = "범위를 입력해주세요.")
    private Scope scope;//범위

    private Long roomId;// 스터디룸 아이디

    @NotBlank(message = "이유를 입력해주세요.")
    private String reason;//이유

    @NotNull(message = "시작 시간을 입력해주세요.")
    private LocalDateTime startAt;//시작 시간

    @NotNull(message = "종료 시간을 입력해주세요.")
    private LocalDateTime endAt;//종료 시간

}
