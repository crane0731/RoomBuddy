package roombuddy.roombuddy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.MemberRole;

import java.time.LocalDateTime;

/**
 * 회원
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private Long memberId;//PK

    private String email;//이메일

    private String password;//패스워드

    private String name;//이름

    private String phone;//전화번호

    private MemberRole role;//역할

    private ActiveStatus activeStatus; //활성화 상태

    private LocalDateTime createdAt;//생성일

    private LocalDateTime updatedAt;//수정일

    /**
     * [생성 메서드]
     * @param email 이메일
     * @param password 패스워드
     * @param name 이름
     * @param phone 전화 번호
     * @return Member
     */
    public static Member create(String email, String password, String name, String phone) {
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(password);
        member.setName(name);
        member.setPhone(phone);
        member.setRole(MemberRole.MEMBER);
        member.setActiveStatus(ActiveStatus.ACTIVE);
        return member;
    }

}
