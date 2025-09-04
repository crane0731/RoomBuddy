package roombuddy.roombuddy.dto.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 목록 응답 DTO
 */
@Getter
@Setter
public class MemberListResponseDto {

    private Long memberId; //회원 아이디
    private String email; //이메일
    private String name;//이름
    private Long reservationCount;//예약 횟수


}
