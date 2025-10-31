package roombuddy.roombuddy.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import roombuddy.roombuddy.dto.reservation.SearchReservationCondDto;
import roombuddy.roombuddy.jpadomain.Reservation;

/**
 * 예약 레파지토리 커스텀
 */
public interface ReservationRepositoryCustom {

    Page<Reservation> findAllReservationsByRoomAndCond(@Param("roomId") Long roomId, @Param("cond") SearchReservationCondDto cond, Pageable pageable);


}
