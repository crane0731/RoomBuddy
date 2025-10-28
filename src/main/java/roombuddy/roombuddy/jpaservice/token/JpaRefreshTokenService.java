package roombuddy.roombuddy.jpaservice.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.config.jwt.JpaTokenProvider;
import roombuddy.roombuddy.enums.TokenType;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.JwtCustomException;
import roombuddy.roombuddy.jpadomain.Member;
import roombuddy.roombuddy.jpadomain.Token;
import roombuddy.roombuddy.repository.TokenRepository;

import java.time.Duration;
import java.util.List;

/**
 * 리프레쉬 토큰 서비스
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class JpaRefreshTokenService {

    private final TokenRepository tokenRepository;//토큰 레파지토리
    private final JpaTokenProvider tokenProvider; //토큰 프로바이더 서비스

    private final Duration refreshTokenValidity = Duration.ofDays(1);  // Refresh Token 유효 시간 (1일)

    /**
     * [조회]
     * @param memberId 회원 아이디
     * @return Token
     */
    public List<Token> findByMemberId(Long memberId) {
        return tokenRepository.findByMemberId(memberId);
    }


    /**
     * [삭제]
     * 회원을 통해 리프레쉬 토큰 삭제
     * @param memberId 회원 아이디
     * */
    @Transactional
    public void delete(Long memberId) {
        tokenRepository.deleteByMemberId(memberId);
    }

    /**
     * 리프레쉬 토큰 조회
     * @param refreshToken 리프레쉬 토큰
     * @return RefreshToken
     */
    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new JwtCustomException(ErrorMessage.NOT_FOUND_REFRESH_TOKEN));
    }


    /**
     * 리프레쉬 토큰 생성 , 저장
     * @param member 회원
     */
    @Transactional
    public String createRefreshToken(Member member) {

        //리프레쉬 토큰 생성
        String token = tokenProvider.generateToken(member, refreshTokenValidity, TokenType.REFRESH);

        //리프레쉬 토큰 저장
        tokenRepository.save(Token.create(member, token));

        return token;

    }

}