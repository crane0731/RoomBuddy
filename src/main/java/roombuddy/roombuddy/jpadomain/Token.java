package roombuddy.roombuddy.jpadomain;

import jakarta.persistence.*;
import lombok.Getter;
import roombuddy.roombuddy.jpadomain.baseentity.BaseTimeEntity;

/**
 * 토큰
 */
@Entity
@Table(name = "token")
@Getter
public class Token extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //회원 아이디

    @Column(name = "refresh_token")
    private String refreshToken;//리프레쉬 토큰


    /**
     * [생성 메서드]
     * @param member 회원
     * @param refreshToken 리프레쉬 토큰
     * @return Token
     */
    public static Token create(Member member, String refreshToken) {
        Token token = new Token();
        token.member = member;
        token.refreshToken = refreshToken;
        return token;
    }
}
