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
}
