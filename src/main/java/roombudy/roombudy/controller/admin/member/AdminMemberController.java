package roombudy.roombudy.controller.admin.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roombudy.roombudy.dto.api.ApiResponseDto;
import roombudy.roombudy.dto.member.SearchMemberCondDto;
import roombudy.roombudy.enums.MemberSortType;
import roombudy.roombudy.service.admin.member.AdminMemberService;

import java.util.Map;

/**
 * 관리자 회원 관리 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombudy/admin/members")
public class AdminMemberController {


    private final AdminMemberService adminMemberService;//관리자 회원 서비스

    /**
     * [컨트롤러]
     * 관리자 - 회원 목록 조회
     * @param name 이름
     * @param email 이메일
     * @param sortType 정렬 타입
     * @param page 페이지 번호
     * @return PagedResponseDto
     */
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<?>> getMembers(@RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "email",required = false)String email,
                                                        @RequestParam(value = "sortType",required = false)MemberSortType sortType,
                                                        @RequestParam(value = "page",defaultValue = "0") int page
                                                        ) {

        return ResponseEntity.ok(ApiResponseDto.success(adminMemberService.getMembers(SearchMemberCondDto.create(name, email, sortType), page)));

    }

    /**
     * [컨트롤러]
     * 관리자 - 회원의 정보 상세 정보
     * @param id 회원 아이디
     * @return MemberInfoResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getMemberInfo(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponseDto.success(adminMemberService.getMemberInfo(id)));
    }

    /**
     * [컨트롤러]
     * 관리자 회원 삭제
     * @param id 회원 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteMember(@PathVariable Long id) {
        adminMemberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "회원 삭제 성공")));

    }



}
