package roombudy.roombudy.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import roombudy.roombudy.domain.Member;
import roombudy.roombudy.dto.member.MemberInfoResponseDto;
import roombudy.roombudy.dto.member.MemberListResponseDto;
import roombudy.roombudy.dto.member.SearchMemberCondDto;

import java.util.List;
import java.util.Optional;

/**
 * 회원 매퍼
 */
@Mapper
public interface MemberMapper {

    int save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<MemberInfoResponseDto> findMemberInfoById(Long id);

    void updateNameById(@Param("id") Long id,@Param("name") String name);

    void updatePhoneById(@Param("id") Long id,@Param("phone") String phone);

    void updatePasswordById(@Param("id") Long id,@Param("password") String password);

    void softDelete(Long id);

    List<MemberListResponseDto> findAllByCond(@Param("cond") SearchMemberCondDto cond,@Param("offset") int offset, @Param("limit") int limit);

    Long countByCond(@Param("cond") SearchMemberCondDto cond);



}
