package roombudy.roombudy.dto.blackout;

import lombok.Getter;
import lombok.Setter;

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
}
