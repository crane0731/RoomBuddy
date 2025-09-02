package roombudy.roombudy.service.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombudy.roombudy.dao.mapper.ReservationMapper;
import roombudy.roombudy.domain.Reservation;
import roombudy.roombudy.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 스케줄 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationScheduledService {

    private final ReservationMapper reservationMapper;//예약 매퍼

    /**
     * 매일 09시 ~ 22시 사이 1시간마다 정각에 실행
     */
    @Scheduled(cron = "0 0 9-22 * * *")
    public void completedReservation() {
        log.info("스케쥴러 시작됨");


       //오늘의 예약 리스트 조회
        List<Reservation> reservations = reservationMapper.findTodayConfirmedReservation();

        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals(ReservationStatus.CONFIRMED)){
                reservationMapper.updateStatusForCompleted(reservation.getReservationId());
            }
        }


    }
}
