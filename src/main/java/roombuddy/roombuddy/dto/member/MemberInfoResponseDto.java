package roombuddy.roombuddy.dto.member;


import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.enums.MemberRole;
import roombuddy.roombuddy.utils.DateFormatUtil;

import java.time.LocalDateTime;

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

    /**
     * 생성자
     * @param memberId 회원 아이디
     * @param email 이메일
     * @param name 이름
     * @param phone 전화 번호
     * @param role 역할
     * @param reservationCount 예약 횟수
     * @param createdAt 생성일
     */
    public MemberInfoResponseDto(Long memberId, String email, String name, String phone, MemberRole role, Long reservationCount, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.role = role.name();
        this.reservationCount = reservationCount;
        this.createdAt = DateFormatUtil.DateFormat(createdAt);
    }
}
