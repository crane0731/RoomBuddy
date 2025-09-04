package roombuddy.roombuddy.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.config.jwt.TokenProvider;
import roombuddy.roombuddy.dao.mapper.TokenMapper;
import roombuddy.roombuddy.domain.Member;
import roombuddy.roombuddy.domain.Token;
import roombuddy.roombuddy.enums.TokenType;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.JwtCustomException;

import java.time.Duration;
import java.util.List;

/**
 * 리프레쉬 토큰 서비스
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final TokenMapper tokenMapper;
    private final TokenProvider tokenProvider;

    private final Duration refreshTokenValidity = Duration.ofDays(1);  // Refresh Token 유효 시간 (1일)

    /**
     * [조회]
     * @param memberId 회원 아이디
     * @return Token
     */
    public List<Token> findByMemberId(Long memberId) {
        return tokenMapper.findByMemberId(memberId);
    }


    /**
     * [삭제]
     * 회원을 통해 리프레쉬 토큰 삭제
     * @param memberId 회원 아이디
     * */
    @Transactional
    public void delete(Long memberId) {

        tokenMapper.deleteByMemberId(memberId);
    }

    /**
     * 리프레쉬 토큰 조회
     * @param refreshToken 리프레쉬 토큰
     * @return RefreshToken
     */
    public Token findByRefreshToken(String refreshToken) {
        return tokenMapper.findByRefreshToken(refreshToken).orElseThrow(()->new JwtCustomException(ErrorMessage.NOT_FOUND_REFRESH_TOKEN));
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
        tokenMapper.save(Token.create(member.getMemberId(), token));

        return token;

    }

}