package roombudy.roombudy.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import roombudy.roombudy.domain.Token;

import java.util.List;
import java.util.Optional;

/**
 *  토큰 매퍼
 */
@Mapper
public interface TokenMapper {

    int save(Token token);

    List<Token> findByMemberId(Long memberId);

    Optional<Token> findByRefreshToken(String refreshToken);

    void delete(Long id);

    void deleteByMemberId(Long memberId);

}
