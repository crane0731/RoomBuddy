package roombuddy.roombuddy.repository;

import jakarta.persistence.LockModeType;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dto.reservation.MyReservationListResponseDto;
import roombuddy.roombuddy.dto.reservation.ReservationListResponseDto;
import roombuddy.roombuddy.dto.reservation.ReservationSlotDto;
import roombuddy.roombuddy.dto.reservation.SearchReservationCondDto;
import roombuddy.roombuddy.jpadomain.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Jpa 예약 레파지토리
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

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


    @Query("SELECT rv " +
            "FROM Reservation rv " +
            "WHERE rv.activeStatus='ACTIVE'" +
            "AND rv.status='CONFIRMED' " +
            "AND rv.startAt < :endAt " +
            "AND rv.endAt > :startAt")
    List<Reservation> findOverlappingReservations(@Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);


    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE) // == FOR UPDATE
    @Query("SELECT rv " +
            "FROM Reservation rv " +
            "WHERE rv.room.id=:roomId " +
            "AND rv.activeStatus='ACTIVE'" +
            "AND rv.status='CONFIRMED' " +
            "AND rv.startAt < :endAt " +
            "AND rv.endAt > :startAt")
    List<Reservation> findOverlappingReservationsByRoomIdForUpdate(@Param("roomId") Long roomId, @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    @Query("SELECT rv " +
            "FROM Reservation rv " +
            "JOIN fetch rv.member m " +
            "WHERE rv.room.id=:roomId " +
            " AND rv.status IN (roombuddy.roombuddy.enums.ReservationStatus.CONFIRMED,\n" +
            "                    roombuddy.roombuddy.enums.ReservationStatus.COMPLETED)" +
            "AND rv.activeStatus='ACTIVE' " +
            "AND FUNCTION('DATE', rv.startAt) = :targetDate "
    )
    List<Reservation> findReservationsByRoomAndDate(@Param("roomId") Long roomId, @Param("targetDate") LocalDate targetDate);


    @Query("SELECT rv " +
            "FROM Reservation rv " +
            "JOIN fetch rv.room r " +
            "WHERE rv.member.id = :memberId " +
            "AND rv.activeStatus = 'ACTIVE' " +
            "AND rv.startAt > NOW() " +
            "ORDER BY rv.createdAt DESC"
    )
    List<Reservation> findMyReservationByMemberIdAndDate(@Param("memberId") Long memberId);


    @Query("SELECT rv " +
            "FROM Reservation rv " +
            "JOIN FETCH rv.member m " +
            "WHERE rv.activeStatus = 'ACTIVE' " +
            "AND rv.room.id = :roomId " +
            "AND rv.status='CONFIRMED' " +
            "ORDER BY  rv.createdAt DESC")
    Page<Reservation>findConfirmedReservationByRoom(@Param("roomId") Long roomId,Pageable pageable);


}
