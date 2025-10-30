package roombuddy.roombuddy.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roombuddy.roombuddy.dto.reservation.ReservationSlotDto;
import roombuddy.roombuddy.jpadomain.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Jpa 예약 레파지토리
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
    SELECT new roombuddy.roombuddy.dto.reservation.ReservationSlotDto(
        rv.startAt,
        rv.endAt
    )
    FROM Reservation rv
    WHERE rv.room.id = :roomId
      AND FUNCTION('DATE', rv.startAt) = :targetDate
      AND rv.activeStatus = 'ACTIVE'
      AND rv.status = 'CONFIRMED'
    ORDER BY rv.startAt
""")
    List<ReservationSlotDto> findReservedSlots(
            @Param("roomId") Long roomId,
            @Param("targetDate") LocalDate targetDate
    );


    @Query( "SELECT r " +
            "FROM Reservation r " +
            "WHERE r.activeStatus='ACTIVE'" +
            "AND r.status='CONFIRMED' " +
            "AND r.startAt < :startAt " +
            "AND r.endAt > :endAt")
    List<Reservation> findOverlappingReservations(@Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);



}
