package roombuddy.roombuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roombuddy.roombuddy.jpadomain.Token;

import java.util.List;
import java.util.Optional;

/**
 * JPA 토큰 레파지토리
 */
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    Optional<Token> findByRefreshToken(String refreshToken);

}
