package roombuddy.roombuddy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import roombuddy.roombuddy.dto.member.MemberListResponseDto;
import roombuddy.roombuddy.dto.member.SearchMemberCondDto;

/**
 * 커스텀 회원 레파지토리
 */
public interface MemberRepositoryCustom {

    Page<MemberListResponseDto> finAllByCond(SearchMemberCondDto cond, Pageable pageable);

}
