package roombuddy.roombuddy.dto.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 정보 응답 DTO
 */
@Getter
@Setter
public class MemberInfoResponseDto {

    private Long memberId; //회원 아이디

    private String email; //이메일

    private String name; //이름

    private String phone; //전화 번호

    private String  role;//역할

    private Long reservationCount;//예약 횟수

    private String createdAt;//생성일

}
