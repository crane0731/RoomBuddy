package roombuddy.roombuddy.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roombuddy.roombuddy.dto.blackout.BlackoutDto;
import roombuddy.roombuddy.jpadomain.Blackout;

import java.time.LocalDate;
import java.util.List;

/**
 * Jpa 블랙 아웃 레파지토리
 */
public interface BlackoutRepository extends JpaRepository<Blackout, Long> {

    @Query("""
    SELECT new roombuddy.roombuddy.dto.blackout.BlackoutDto(
        b.startAt,
        b.endAt
    )
    FROM Blackout b
    WHERE b.room.id=:roomId
        AND FUNCTION('DATE', b.startAt) = :targetDate
        AND b.activeStatus='ACTIVE'
    Order by b.startAt
   
    """)
    List<BlackoutDto> findBlackoutsByDate(@Param("roomId") Long roomId, @Param("targetDate") LocalDate targetDate);


}
