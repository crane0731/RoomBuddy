package roombuddy.roombuddy.jpaservice.userdetail;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.jpadomain.Member;
import roombuddy.roombuddy.jpadomain.userdetail.JpaCustomUserDetails;
import roombuddy.roombuddy.repository.MemberRepository;

/**
 * CustomUserDetails를 생성하는 서비스 -> Spring Security에 필요
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaCustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;//회원 레파지토리


    /**
     * 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(ErrorMessage.NOT_FOUND_MEMBER));

        return new JpaCustomUserDetails(member);
    }



}
