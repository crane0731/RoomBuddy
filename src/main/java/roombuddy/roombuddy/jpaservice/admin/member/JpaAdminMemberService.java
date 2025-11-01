package roombuddy.roombuddy.jpaservice.admin.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dto.api.PagedResponseDto;
import roombuddy.roombuddy.dto.member.MemberInfoResponseDto;
import roombuddy.roombuddy.dto.member.MemberListResponseDto;
import roombuddy.roombuddy.dto.member.SearchMemberCondDto;
import roombuddy.roombuddy.jpadomain.Member;
import roombuddy.roombuddy.jpaservice.member.JpaMemberService;
import roombuddy.roombuddy.jpaservice.token.JpaRefreshTokenService;

/**
 * 관리자 회원 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaAdminMemberService {

    private final JpaRefreshTokenService refreshTokenService; //리프레쉬 토큰 서비스

    private final JpaMemberService memberService;//회원 서비스


    /**
     * [서비스 로직]
     * 관리자 - 회원 삭제
     * @param memberId 회원 아이디
     */
    @Transactional
    public void deleteMember(Long memberId) {

        //회원 조회
        Member member = memberService.findById(memberId);

        //SOFT DELETE
        member.softDelete();

        //리프레쉬 토큰 삭제
        refreshTokenService.delete(memberId);

    }


    /**
     * [서비스 로직]
     * 회원의 정보 상세 조회
     * @return MemberInfoResponseDto
     */
    public MemberInfoResponseDto getMemberInfo(Long memberId){
        //응답 DTO 반환
        return memberService.getMemberInfoResponseDto(memberId);
    }


    /**
     * [서비스 로직]
     * 관리자 - 회원 목록 조회
     * @param cond 검색 조건 DTO
     * @param page 페이지 번호
     * @return PagedResponseDto<MemberListResponseDto>
     */
    public PagedResponseDto<MemberListResponseDto> getMembers(SearchMemberCondDto cond, int page){
        return memberService.getPagedMemberListResponseDto(cond,page);
    }




}
