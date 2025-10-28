package roombuddy.roombuddy.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roombuddy.roombuddy.dto.api.ApiResponseDto;
import roombuddy.roombuddy.dto.login.LoginRequestDto;
import roombuddy.roombuddy.dto.login.SendEmailRequestDto;
import roombuddy.roombuddy.dto.login.SignUpRequestDto;
import roombuddy.roombuddy.dto.login.ValidateEmailCodeRequestDto;
import roombuddy.roombuddy.dto.token.TokenResponseDto;
import roombuddy.roombuddy.jpaservice.login.JpaLoginService;
import roombuddy.roombuddy.service.email.EmailCodeService;
import roombuddy.roombuddy.service.login.LoginService;
import roombuddy.roombuddy.utils.ErrorCheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 로그인 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/auth")
public class LoginController {

    private final LoginService loginService;//로그인 서비스
    private final JpaLoginService jpaLoginService;//Jpa 로그인 서비스
    private final EmailCodeService emailCodeService;//이메일 인증 코드 서비스

    /**
     * [컨트롤러]
     * 회원 가입
     * @param requestDto 회원 가입 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<?>>signUp(@Valid @RequestBody SignUpRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        jpaLoginService.signUp(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "회원가입 성공")));

    }

    /**
     * [컨트롤러]
     * 인증 메일 보내기
     * @param requestDto 메일 전송 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("/signup/email")
    public ResponseEntity<ApiResponseDto<?>>sendEmail(@Valid @RequestBody SendEmailRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        emailCodeService.sendEmailCode(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "인증 메일 전송 성공")));

    }

    /**
     * [컨트롤러]
     * 이메일 인증
     * @param requestDto 인증 요청 DTO
     * @param bindingResult 에러메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("/signup/email/validate")
    public ResponseEntity<ApiResponseDto<?>>validateEmailCode(@Valid @RequestBody ValidateEmailCodeRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        emailCodeService.validateCode(requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "인증 메일 인증 성공")));

    }


    /**
     * [컨트롤러]
     * 로그인
     * @param loginRequestDto 로그인 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return TokenResponseDto
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<?>> login(@Valid @RequestBody LoginRequestDto loginRequestDto, BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (ErrorCheckUtil.errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //로그인
        TokenResponseDto tokenResponseDto = jpaLoginService.login(loginRequestDto);

        return ResponseEntity.ok().body(ApiResponseDto.success(tokenResponseDto));

    }

    /**
     * [컨트롤러]
     * 로그아웃
     * @param request HTTP 요청
     * @return 성공메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<?>> logout(HttpServletRequest request) {

        //로그아웃
        jpaLoginService.logout(request);

        return ResponseEntity.ok(ApiResponseDto.success(Map.of("message", "로그아웃 성공")));

    }

}
