package roombudy.roombudy.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * 스터디룸 생성 요청 DTO
 */
@Getter
@Setter
public class CreateRoomRequestDto {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name; //이름

    @NotNull(message = "수용 인원을 입력해 주세요.")
    private Long capacity;//수용 인원

    @NotNull(message = "운영 시작 시간을 입력해 주세요.")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "HH:mm")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @NotNull(message = "운영 종료 시간을 입력해 주세요.")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "HH:mm")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    // 종료 > 시작 검증 (자정 넘기는 야간 운영은 미허용)
    @jakarta.validation.constraints.AssertTrue(message = "운영 종료시간은 시작시간보다 이후여야 합니다.")
    public boolean isValidHours() {
        if (openTime == null || closeTime == null) return true;
        return closeTime.isAfter(openTime);
    }

}
