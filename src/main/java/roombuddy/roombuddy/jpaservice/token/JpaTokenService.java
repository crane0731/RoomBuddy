package roombuddy.roombuddy.jpaservice.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.config.jwt.JpaTokenProvider;

import roombuddy.roombuddy.enums.TokenType;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.JwtCustomException;
import roombuddy.roombuddy.exception.MemberCustomException;
import roombuddy.roombuddy.jpadomain.Member;
import roombuddy.roombuddy.repository.MemberRepository;


import java.time.Duration;

/**
 * 토큰 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaTokenService {
    private final JpaTokenProvider tokenProvider;
    private final JpaRefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;//회원 레파지토리


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

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMember().getId();


        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberCustomException(ErrorMessage.NOT_FOUND_MEMBER));

        return tokenProvider.generateToken(member, Duration.ofHours(5), TokenType.ACCESS);

    }

}
