package roombuddy.roombuddy.dto.blackout;

import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.jpadomain.Blackout;
import roombuddy.roombuddy.utils.DateFormatUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 블랙 아웃 응답 DTO
 */
@Getter
@Setter
public class BlackoutListResponseDto {

    private Long blackoutId; //블랙 아웃 아이디

    private String scope; //범위

    private String reason; //이유

    private String startAt; //시작 시간

    private String endAt; //종료 시간

    private String createdAt; //생성일


    /**
     * [생성 메서드]
     * @param blackout 블랙 아웃
     * @return BlackoutListResponseDto
     */
    public static BlackoutListResponseDto create(Blackout blackout) {
        BlackoutListResponseDto dto = new BlackoutListResponseDto();
        dto.blackoutId = blackout.getId();
        dto.scope = blackout.getScope().name();
        dto.reason = blackout.getReason();
        dto.startAt = DateFormatUtil.DateFormat(blackout.getStartAt());;
        dto.endAt = DateFormatUtil.DateFormat(blackout.getEndAt());;
        dto.createdAt = DateFormatUtil.DateFormat(blackout.getCreatedAt());
        return dto;
    }
}
