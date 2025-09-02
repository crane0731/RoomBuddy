package roombudy.roombudy.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombudy.roombudy.config.jwt.TokenProvider;
import roombudy.roombudy.dao.mapper.MemberMapper;
import roombudy.roombudy.domain.Member;
import roombudy.roombudy.enums.TokenType;
import roombudy.roombudy.exception.ErrorMessage;
import roombudy.roombudy.exception.JwtCustomException;
import roombudy.roombudy.exception.MemberCustomException;

import java.time.Duration;
/**
 * 토큰 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberMapper memberMapper;

    /**
     * 리프레쉬 토큰을 통해 새로운 엑세스 토큰을 생성
     * @param refreshToken
     * @return
     */
    public String createNewAccessToken(String refreshToken) throws JwtCustomException {

        //토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validRefreshToken(refreshToken)) {
            throw new JwtCustomException(ErrorMessage.INVALID_TOKEN);
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberMapper.findById(memberId).orElseThrow(()-> new MemberCustomException(ErrorMessage.NOT_FOUND_MEMBER));

        return tokenProvider.generateToken(member, Duration.ofHours(5), TokenType.ACCESS);

    }

}
