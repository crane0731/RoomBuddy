package roombuddy.roombuddy.service.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.domain.Member;
import roombuddy.roombuddy.domain.Token;
import roombuddy.roombuddy.domain.userdetail.CustomUserDetails;
import roombuddy.roombuddy.dto.login.LoginRequestDto;
import roombuddy.roombuddy.dto.login.SignUpRequestDto;
import roombuddy.roombuddy.dto.token.TokenResponseDto;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.MemberCustomException;
import roombuddy.roombuddy.service.member.MemberService;
import roombuddy.roombuddy.service.token.RefreshTokenService;
import roombuddy.roombuddy.service.token.TokenBlackListService;
import roombuddy.roombuddy.service.token.TokenService;

import java.util.List;

/**
 * 로그인 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberService memberService; //회원 서비스
    private final TokenService tokenService; //토큰 서비스
    private final RefreshTokenService refreshTokenService; //리프레쉬 토큰 서비스
    private final TokenBlackListService tokenBlackListService; //토큰 블랙리스트 서비스

    private final AuthenticationManager authenticationManager; //시큐리티 인증 매니저
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //패스워드 인코더

    /**
     * [회원 가입]
     * @param dto 회원 가입 요청 DTO
     */
    @Transactional
    public void signUp(SignUpRequestDto dto){

        //회원가입 체크
        signupCheck(dto);

        //패스워드 인코딩
        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

        //회원 도메인 생성
        Member member = Member.create(dto.getEmail(), encodedPassword, dto.getName(), dto.getPhone());

        //회원 저장
        memberService.save(member);

    }
    /**
     * [서비스 로직]
     * 회원 로그인
     * @param dto 로그인 요청 DTO
     * @return TokenResponseDto
     */
    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {

        //스프링 시큐리티 수동 로그인
        Member member = securityLogin(dto);

        //JWT 토큰 생성 , 반환
        return makeToken(member);
    }

    /**
     * [서비스 로직]
     * 로그아웃
     * @param request HTTP 요청
     */
    @Transactional
    public void logout(HttpServletRequest request) {

        //현재 로그인한 회원 조회
        Member member = memberService.getLoginMember();

        //현재 로그인한 회원의 모든 리프레쉬 토큰 조회
        List<Token> tokens = refreshTokenService.findByMemberId(member.getMemberId());

        //이미 로그아웃한 회원인지 검증
        validateAlreadyLogOutMember(tokens);

        //리프레쉬 토큰 삭제
        refreshTokenService.delete(member.getMemberId());

        //엑세스 토큰 블랙리스트 등록
        setBlackListAccessToken(request);
    }

    //==엑세스 토큰 블랙리스트 등록==//
    private void setBlackListAccessToken(HttpServletRequest request) {
        //요청에서 인증 헤더 추출
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);

            //블랙리스트에 등록
            tokenBlackListService.blackList(accessToken);
        }
    }

    //==이미 로그아웃한 회원인지 검증==//
    private void validateAlreadyLogOutMember(List<Token> tokens) {
        if(tokens.isEmpty()){
            throw new MemberCustomException(ErrorMessage.ALREADY_LOGOUT_MEMBER);
        }
    }


    //==스프링 시큐리티 수동 로그인==//
    private Member securityLogin(LoginRequestDto dto) {
        //UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());


        //AuthenticationManager로 인증 시도 (loadUser + password 체크 내부 수행)
        Authentication authentication = authenticationManager.authenticate(authToken);


        //인증 정보 SecurityContext에 저장 (로그인 처리)
        SecurityContextHolder.getContext().setAuthentication(authentication);


        //로그인된 회원 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


        return memberService.findByEmail(userDetails.getUsername());
    }

    //==리프레쉬 토큰 + 엑세스 토큰 생성==//
    private TokenResponseDto makeToken(Member member) {
        //리프레시 토큰 생성
        String refreshToken = refreshTokenService.createRefreshToken(member);

        //엑세스 토큰 생성
        String accessToken = tokenService.createNewAccessToken(refreshToken);

        //응답 DTO 반환
        return TokenResponseDto.create(accessToken, refreshToken);
    }

    //==회원가입 검증 로직==//
    public void signupCheck(SignUpRequestDto signUpRequestDto) {

        //이메일 중복 검사
        emailDuplicatedCheck(signUpRequestDto.getEmail());

        //전화번호 중복 검사
        phoneDuplicatedCheck(signUpRequestDto.getPhone());

        //비밀번호 일치 검사
        passwordCheck(signUpRequestDto.getPassword(), signUpRequestDto.getPasswordCheck());

    }

    //==이메일 중복 검사 로직 ==//
    private void emailDuplicatedCheck(String email) {
        if(memberService.existsByEmail(email)) {
            throw new MemberCustomException(ErrorMessage.DUPLICATED_EMAIL);
        }
    }

    //==전화 번호 일치 검사 로직==//
    private void phoneDuplicatedCheck(String phone) {
        if(memberService.existsByPhone(phone)){
            throw new MemberCustomException(ErrorMessage.DUPLICATED_PHONE);
        }
    }

    //==비밀번호 일치 검사 로직==//
    private void passwordCheck(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)){
            throw new MemberCustomException(ErrorMessage.PASSWORD_MISMATCH);
        }
    }

}
