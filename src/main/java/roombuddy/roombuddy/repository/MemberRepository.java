package roombuddy.roombuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roombuddy.roombuddy.dto.member.MemberInfoResponseDto;
import roombuddy.roombuddy.jpadomain.Member;

import java.util.Optional;

/**
 * JPA 회원 레파지토리
 */
public interface MemberRepository extends JpaRepository<Member,Long> ,MemberRepositoryCustom {

    @Query("SELECT m From Member m WHERE m.email= :email")
    Optional<Member>findByEmail(String email);

    @Query("SELECT CASE WHEN count(m) >0 THEN TRUE ELSE FALSE END FROM Member m WHERE m.email= :email")
    Boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN count(m) >0 THEN TRUE ELSE FALSE END FROM Member m WHERE m.phone= :phone ")
    Boolean existsByPhone(String phone);

    @Query("""
        SELECT new roombuddy.roombuddy.dto.member.MemberInfoResponseDto(
            m.id,
            m.email,
            m.name,
            m.phone,
            m.role,
            COALESCE(COUNT (rv.id),0),
            m.createdAt
        )
        FROM Member m
        LEFT JOIN Reservation rv
            ON rv.id = m.id
            AND rv.activeStatus = 'ACTIVE'
            AND rv.status IN(roombuddy.roombuddy.enums.ReservationStatus.COMPLETED)
        WHERE m.id =:id
            AND m.activeStatus='ACTIVE'
        GROUP BY m.id, m.email, m.name, m.phone, m.role, m.createdAt
    """)
    Optional<MemberInfoResponseDto> findMemberInfoById(Long id);

}
