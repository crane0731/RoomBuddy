package roombudy.roombudy.service.userdetail;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombudy.roombudy.dao.mapper.MemberMapper;
import roombudy.roombudy.domain.Member;
import roombudy.roombudy.domain.userdetail.CustomUserDetails;
import roombudy.roombudy.exception.ErrorMessage;

/**
 * CustomUserDetails를 생성하는 서비스 -> Spring Security에 필요
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;


    /**
     * 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberMapper.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(ErrorMessage.NOT_FOUND_MEMBER));

        return new CustomUserDetails(member);
    }



}
