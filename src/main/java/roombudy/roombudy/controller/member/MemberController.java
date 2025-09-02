package roombudy.roombudy.controller.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import roombudy.roombudy.dto.api.ApiResponseDto;
import roombudy.roombudy.dto.member.UpdateMemberNameRequestDto;
import roombudy.roombudy.dto.member.UpdateMemberPasswordRequestDto;
import roombudy.roombudy.dto.member.UpdateMemberPhoneRequestDto;
import roombudy.roombudy.service.member.MemberService;
import roombudy.roombudy.utils.ErrorCheckUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombudy/members")
public class MemberController {

    private final MemberService memberService;//회원 서비스

    /**
     * [컨트롤러]
     * 자신의 정보 상세 조회
     * @return MemberInfoResponseDto
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<?>> getMyMemberInfo(){
        return ResponseEntity.ok(ApiResponseDto.success(memberService.getMyMemberInfo()));
    }


    /**
     * [컨트롤러]
     * 회원 이름 수정
     * @param requestDto 수정 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PutMapping("/me/name")
    public ResponseEntity<ApiResponseDto<?>>updateName(@Valid @RequestBody UpdateMemberNameRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        memberService.updateName(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "이름 수정 성공")));

    }

    /**
     * [컨트롤러]
     * 회원 전화번호 수정
     * @param requestDto 수정 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PutMapping("/me/phone")
    public ResponseEntity<ApiResponseDto<?>>updatePhone(@Valid @RequestBody UpdateMemberPhoneRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        memberService.updatePhone(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "전화번호 수정 성공")));

    }

    /**
     * [컨트롤러]
     * 회원 비밀번호 수정
     * @param requestDto 수정 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponseDto<?>>updatePassword(@Valid @RequestBody UpdateMemberPasswordRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        memberService.updatePassword(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "비밀번호 수정 성공")));

    }

    /**
     * [컨트롤러]
     * 회원 탈퇴
     * @return 성공 메시지
     */
    @DeleteMapping("/me/withdraw")
    public ResponseEntity<ApiResponseDto<?>> withdrawMember(HttpServletRequest request){

        memberService.withdraw(request);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "회원 탈퇴 성공")));

    }

}
