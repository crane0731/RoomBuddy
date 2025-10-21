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

    @Column(name = "email")
    private String email;//이메일

    @Column(name = "password")
    private String password;//패스워드

    @Column(name = "name")
    private String name;//이름

    @Column(name = "phone")
    private String phone;//전화번호

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private MemberRole role;//역할

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status",nullable = false)
    private ActiveStatus activeStatus; //활성화 상태


    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        this.name="삭제된 회원 이름";
        this.email=null;
        this.password=null;
        this.phone=null;
        this.activeStatus=ActiveStatus.INACTIVE;

    }

    /**
     * [비즈니스 로직]
     * 이름 업데이트
     * @param newName 새로운 이름
     */
    public void updateName(String newName){
        this.name=newName;
    }

    /**
     * [비즈니스 로직]
     * 전화번호 업데이트
     * @param newPhone 새로운 전화 번호
     */
    public void updatePhone(String newPhone){
        this.phone=newPhone;
    }

    /**
     * [비즈니스 로직]
     * 패스워드 업데이트
     * @param newPassword 새로운 패스워드(인코딩 된)
     */
    public void updatePassword(String newPassword){
        this.password=newPassword;
    }

}
