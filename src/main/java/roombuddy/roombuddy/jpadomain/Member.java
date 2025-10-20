package roombuddy.roombuddy.jpadomain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.MemberRole;
import roombuddy.roombuddy.jpadomain.baseentity.BaseTimeEntity;


/**
 * 회원 정보
 */
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;//PK

    @Column(name = "email",unique = true)
    private String email;//이메일

    @Column(name = "password")
    private String password;//패스워드

    @Column(name = "name")
    private String name;//이름

    @Column(name = "phone",unique = true)
    private String phone;//전화번호

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private MemberRole role;//역할

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status",nullable = false)
    private ActiveStatus activeStatus; //활성화 상태

}
