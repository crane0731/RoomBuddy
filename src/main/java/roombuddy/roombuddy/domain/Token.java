package roombudy.roombudy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roombudy.roombudy.enums.ActiveStatus;

import java.time.LocalDateTime;

/**
 * 토큰
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    private Long tokenId; //PK

    private Long memberId; //회원 아이디

    private String refreshToken;//리프레쉬 토큰

    private ActiveStatus activeStatus; //활성화 상태

    private LocalDateTime createdAt;//생성일

    private LocalDateTime updatedAt;//수정일

    /**
     * [생성 메서드]
     * @param memberId 회원 아이디
     * @param refreshToken 리프레쉬 토큰
     * @return Token
     */
    public static Token create(Long memberId,String refreshToken){
        Token token = new Token();
        token.memberId = memberId;
        token.refreshToken = refreshToken;
        token.activeStatus = ActiveStatus.ACTIVE;
        return token;
    }
}
