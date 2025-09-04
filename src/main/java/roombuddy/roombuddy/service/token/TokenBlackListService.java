package roombuddy.roombuddy.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.config.jwt.TokenProvider;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.JwtCustomException;

import java.util.concurrent.TimeUnit;

/**
 * 토큰 블랙리스트 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenBlackListService {

    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    /**
     * 블랙리스트에 토큰을 저장
     * @param token
     */
    public void blackList(String token) {
        //남은 만료 시간
        long ttl = tokenProvider.getAccessTokenExpiration(token);

        if (ttl <= 0) {
            throw new JwtCustomException(ErrorMessage.EXPIRED_TOKEN);
        }
        redisTemplate.opsForValue().set(token,"logout",ttl, TimeUnit.MILLISECONDS);

    }

    /**
     * 토큰이 블랙리스트에 포함되는지 확인
     * @param token
     * @return
     */
    public boolean isBlacklisted(String token) {
        return "logout".equals(redisTemplate.opsForValue().get(token));
    }
}
