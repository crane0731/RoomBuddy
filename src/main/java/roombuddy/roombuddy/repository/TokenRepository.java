package roombuddy.roombuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roombuddy.roombuddy.jpadomain.Token;

/**
 * JPA 토큰 레파지토리
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
}
