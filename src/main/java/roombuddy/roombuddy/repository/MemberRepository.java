package roombuddy.roombuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roombuddy.roombuddy.jpadomain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {


}
