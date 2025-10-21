//package roombuddy.roombuddy.jpaservice.member;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import roombuddy.roombuddy.dto.api.PagedResponseDto;
//import roombuddy.roombuddy.dto.member.*;
//import roombuddy.roombuddy.exception.ErrorMessage;
//import roombuddy.roombuddy.exception.MemberCustomException;
//import roombuddy.roombuddy.jpadomain.Member;
//import roombuddy.roombuddy.repository.MemberRepository;
//import roombuddy.roombuddy.service.token.RefreshTokenService;
//import roombuddy.roombuddy.service.token.TokenBlackListService;
//
//import java.util.List;
//
//
///**
// * JPA 회원 서비스
// */
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class JPaMemberService {
//
//    private final JPaTokenBlackListService tokenBlackListService; //토큰 블랙리스트 서비스
//    private final JPaRefreshTokenService refreshTokenService; //리프레쉬 토큰 서비스
//
//    private final BCryptPasswordEncoder bCryptPasswordEncoder; //패스워드 인코더
//
//    private final MemberRepository memberRepository;//회원 레파지토리
//
//
//    /**
//     * [서비스 로직]
//     * 현재 스프링시큐리티에 로그인된 회원의 정보를 가져오기
//     * @return Member
//     */
//    public Member getLoginMember() {
//
//        //시큐리티 컨텍스트 홀더에서 인증 객체 조회
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        String email = authentication.getName();
//
//        return findByEmail(email);
//
//    }
//
//    /**
//     * [서비스 로직]
//     * 자신의 정보 상세 조회
//     * @return MemberInfoResponseDto
//     */
//    public MemberInfoResponseDto getMyMemberInfo(){
//
//        //현재 로그인한 회원 PK 조회
//        Member member = getLoginMember();
//
//        //응답 DTO 반환
//        return getMemberInfoResponseDto(member.getId());
//    }
//
//    /**
//     * [조회]
//     * 응답 DTO 조회
//     * @param memberId 회원 아이디
//     * @return MemberInfoResponseDto
//     */
//    public MemberInfoResponseDto getMemberInfoResponseDto(Long memberId) {
//        return memberRepository.findMemberInfoById(memberId).orElseThrow(() -> new MemberCustomException(ErrorMessage.NOT_FOUND_MEMBER));
//    }
//
//
//    /**
//     * [서비스 로직]
//     * 회원 이름 수정
//     * @param dto 수정 요청 DTO
//     */
//    @Transactional
//    public void updateName(UpdateMemberNameRequestDto dto){
//
//        //현재 로그인한 회원 PK 조회
//        Member member = getLoginMember();
//
//        //이름 업데이트
//        member.updateName(dto.getName());
//    }
//
//    /**
//     * [서비스 로직]
//     * 회원 전화번호 수정
//     * @param dto 수정 요청 DTO
//     */
//    @Transactional
//    public void updatePhone(UpdateMemberPhoneRequestDto dto){
//
//        //현재 로그인한 회원 조회
//        Member member = getLoginMember();
//
//        //전화번호가 이미 존재하면 에러 반환
//        if (existsByPhone(dto.getPhone())){
//
//            //회원의 기존 전화번호와 변경하려는 전화번호가 같다면 리턴
//            if(member.getPhone().equals(dto.getPhone())){
//                return;
//            }else {
//                throw new MemberCustomException(ErrorMessage.DUPLICATED_PHONE);
//            }
//        }
//
//        //전화 번호 업데이트
//        member.updatePhone(dto.getPhone());
//    }
//
//
//    /**
//     * [컨트롤러]
//     * 회원 비밀번호 수정
//     * @param dto 수정 요청 DTO
//     */
//    @Transactional
//    public void updatePassword(UpdateMemberPasswordRequestDto dto){
//
//        //현재 로그인한 회원 조회
//        Member member = getLoginMember();
//
//        //패스워드 & 패스워드 체크 검증
//        validatePassword(dto);
//
//        //변경할 패스워드 인코딩
//        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
//
//        //비밀번호 업데이트
//        member.updatePassword(encodedPassword);
//
//    }
//
//
//    /**
//     * [서비스 로직]
//     * 회원 탈퇴(SOFT DELETE)
//     */
//    @Transactional
//    public void withdraw(HttpServletRequest request){
//
//        //현재 로그인한 회원의 PK 값
//        Member member = getLoginMember();
//
//        //SOFT DELETE
//        member.softDelete();
//
//        //리프레쉬 토큰 삭제
//        refreshTokenService.delete(memberId);
//
//        //토큰 블랙 리스트 처리
//        setBlackListAccessToken(request);
//    }
//
//
//    /**
//     * [저장]
//     * @param member 회원
//     */
//    @Transactional
//    public void save(Member member) {
//        memberRepository.save(member);
//    }
//
//
//    /**
//     * [서비스 로직]
//     * 페이징된 응답 DTO 생성
//     * @param dto 검색 조건 DTO
//     * @param page 페이지 번호
//     * @return PagedResponseDto<MemberListResponseDto>
//     */
//    public PagedResponseDto<MemberListResponseDto> getPagedMemberListResponseDto(SearchMemberCondDto dto ,int page){
//
//        //페이징 결과 조회
//        Page<MemberListResponseDto> pageResult = memberRepository.finAllByCond(dto, PageRequest.of(page,10));
//
//        //응답 DTO 조회
//        List<MemberListResponseDto> content = pageResult.getContent();
//
//
//        return createPagedResponseDto(content,pageResult);
//
//    }
//
//
//    /**
//     * [조회]
//     * PK 값으로 조회
//     * @param id PK
//     * @return Member
//     */
//    public Member findById(Long id) {
//        return memberRepository.findById(id).orElseThrow(()-> new MemberCustomException(ErrorMessage.NOT_FOUND_MEMBER));
//    }
//
//    /**
//     * [조회]
//     * 이메일로 조회
//     * @param email 이메일
//     * @return Member
//     */
//    public Member findByEmail(String email) {
//        return memberRepository.findByEmail(email).orElseThrow(()->new MemberCustomException(ErrorMessage.NOT_FOUND_MEMBER));
//    }
//
//    /**
//     * [존재 여부 확인]
//     * @param email 이메일
//     * @return boolean
//     */
//    public boolean existsByEmail(String email) {
//        return memberRepository.existsByEmail(email);
//    }
//
//    /**
//     * [존재 여부 확인]
//     * @param phone 전화번호
//     * @return boolean
//     */
//    public boolean existsByPhone(String phone) {
//        return memberRepository.existsByPhone(phone);
//    }
//
//
//
//
//    /**
//     * [서비스 로직]
//     * 엑세스 토큰 블랙리스트 등록
//     * @param request HTTP 요청
//     */
//    public void setBlackListAccessToken(HttpServletRequest request) {
//        //요청에서 인증 헤더 추출
//        String header = request.getHeader("Authorization");
//
//        if (header != null && header.startsWith("Bearer ")) {
//            String accessToken = header.substring(7);
//
//            //블랙리스트에 등록
//            tokenBlackListService.blackList(accessToken);
//        }
//    }
//
//
//    //==패스워드 & 패스워드 체크 검증==//
//    private void validatePassword(UpdateMemberPasswordRequestDto dto) {
//        if(!dto.getPassword().equals(dto.getPasswordCheck())){
//            throw new MemberCustomException(ErrorMessage.PASSWORD_MISMATCH);
//        }
//    }
//
//    //==페이징 응답 DTO 생성==//
//    private PagedResponseDto<MemberListResponseDto> createPagedResponseDto(List<MemberListResponseDto> content, Page<MemberListResponseDto> page) {
//
//        return PagedResponseDto.<MemberListResponseDto>builder()
//                .content(content)
//                .page(page.getNumber())
//                .size(page.getSize())
//                .totalPages(page.getTotalPages())
//                .totalElements(page.getTotalElements())
//                .first(page.isFirst())
//                .last(page.isLast())
//                .build();
//    }
//
//
//
//}
