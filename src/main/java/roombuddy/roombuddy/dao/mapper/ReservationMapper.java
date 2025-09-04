package roombudy.roombudy.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import roombudy.roombudy.domain.Reservation;
import roombudy.roombudy.dto.member.SearchMemberCondDto;
import roombudy.roombudy.dto.reservation.MyReservationListResponseDto;
import roombudy.roombudy.dto.reservation.ReservationListResponseDto;
import roombudy.roombudy.dto.reservation.ReservationSlotDto;
import roombudy.roombudy.dto.reservation.SearchReservationCondDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 예약 매퍼
 */
@Mapper
public interface ReservationMapper {

    int save(Reservation reservation);

    List<ReservationSlotDto> findReservedSlots(@Param("roomId") Long roomId, @Param("targetDate") LocalDate targetDate);

    List<Reservation> findOverlappingReservationsForUpdate(@Param("roomId") Long roomId, @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    List<Reservation> findOverlappingReservations(@Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);



    Optional<Reservation> findById(@Param("id") Long id);

    void softDelete(Long id);

    List<ReservationListResponseDto> findReservationsByRoomAndDate(@Param("roomId") Long roomId, @Param("targetDate") LocalDate targetDate);

    List<MyReservationListResponseDto> findMyReservationByMemberIdAndDate(@Param("memberId") Long memberId);

    List<ReservationListResponseDto> findAllReservationsByRoomAndCond(@Param("roomId")Long roomId, @Param("cond")SearchReservationCondDto cond,@Param("offset") int offset, @Param("limit") int limit);

    Long countByRoomAndCond(@Param("roomId")Long roomId,@Param("cond") SearchReservationCondDto cond);


    List<ReservationListResponseDto>findConfirmedReservationByRoom(@Param("roomId") Long roomId,@Param("offset") int offset, @Param("limit") int limit);

    Long countByRoom(@Param("roomId") Long roomId);

    List<Reservation> findTodayConfirmedReservation();

    void updateStatusForCompleted(Long id);



}
