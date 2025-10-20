package roombuddy.roombuddy.service.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dao.mapper.BlackoutMapper;
import roombuddy.roombuddy.dao.mapper.ReservationMapper;
import roombuddy.roombuddy.dao.mapper.RoomMapper;
import roombuddy.roombuddy.mybatisdomain.Room;
import roombuddy.roombuddy.dto.blackout.BlackoutDto;
import roombuddy.roombuddy.dto.reservation.ReservationSlotDto;
import roombuddy.roombuddy.dto.room.RoomInfoResponseDto;
import roombuddy.roombuddy.dto.room.RoomListResponseDto;
import roombuddy.roombuddy.dto.room.UpdateRoomRequestDto;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.RoomCustomException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 스터디 룸 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomMapper roomMapper;//스터디룸 매퍼
    private final ReservationMapper reservationMapper;//예약 매퍼
    private final BlackoutMapper blackoutMapper;//블랙 아웃 매퍼

    /**
     * [서비스 로직]
     * 스터디룸 목록 조회
     * @return List<RoomListResponseDto>
     */
    public List<RoomListResponseDto> getRoomListForToday() {

        LocalDate today = LocalDate.now();       // 현재 날짜
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜+시간

        // 방 목록 조회
        List<RoomListResponseDto> rooms = roomMapper.findRoomList();

        for (RoomListResponseDto room : rooms) {
            // 오늘 기준 방 예약들 조회 (start~end)
            List<ReservationSlotDto> reservations = reservationMapper.findReservedSlots(room.getRoomId(), today);
            List<BlackoutDto> blackouts = blackoutMapper.findBlackoutsByDate(room.getRoomId(), today);

            boolean available = hasAvailableSlot(room, reservations, blackouts,today, now);
            room.setAvailable(available);
        }

        return rooms;
    }

    /**
     * [서비스 로직]
     * 업데이트
     * @param id 룸 아이디 PK
     * @param dto 수정 요청 DTO
     */
    @Transactional
    public void update(Long id, UpdateRoomRequestDto dto){
        roomMapper.update(id, dto);
    }

    /**
     * [서비스 로직]
     * SOFT DELETE
     * @param id Pk
     */
    @Transactional
    public void softDelete(Long id) {
        roomMapper.softDelete(id);
    }

    /**
     * [서비스 로직]
     * 스터디룸 정보 조회
     * @param id 룸 아이디 PK
     * @return RoomInfoResponseDto
     */
    public RoomInfoResponseDto findRoomInfo(Long id){
        return roomMapper.findRoomInfoById(id).orElseThrow(()->new RoomCustomException(ErrorMessage.NOT_FOUND_ROOM));
    }

    /**
     * [저장]
     * @param room 스터디룸
     * @return int
     */
    @Transactional
    public int save(Room room) {
        return roomMapper.save(room);
    }

    /**
     * [조회]
     * PK값으로 조회
     * @param id PK
     * @return Room
     */
    public Room findById(Long id) {
        return roomMapper.findById(id).orElseThrow(()->new RoomCustomException(ErrorMessage.NOT_FOUND_ROOM));
    }

    //== 현재 예약 가능한 슬롯이 있는지 확인하는 로직 ==//
    private boolean hasAvailableSlot(RoomListResponseDto room,
                                     List<ReservationSlotDto> reservations,
                                     List<BlackoutDto> blackouts,
                                     LocalDate date,
                                     LocalDateTime now) {

        // 예약 + 블랙아웃을 모두 모아서 막힌 슬롯 집합 생성
        Set<LocalDateTime> blockedSlots = new HashSet<>();

        // 예약 구간 -> 1시간 단위 슬롯으로 전개
        for (ReservationSlotDto rv : reservations) {
            LocalDateTime cur = rv.getStartAt();
            while (cur.isBefore(rv.getEndAt())) {
                blockedSlots.add(cur);
                cur = cur.plusHours(1);
            }
        }

        // 블랙아웃 구간 -> 1시간 단위 슬롯으로 전개
        for (BlackoutDto bo : blackouts) {
            LocalDateTime cur = bo.getStartAt();
            while (cur.isBefore(bo.getEndAt())) {
                blockedSlots.add(cur);
                cur = cur.plusHours(1);
            }
        }

        // 방 운영시간 동안의 슬롯 검사
        LocalTime open = room.getOpenTime();
        LocalTime close = room.getCloseTime();

        for (LocalTime t = open; t.isBefore(close); t = t.plusHours(1)) {
            LocalDateTime slot = LocalDateTime.of(date, t);

            // 현재 시각 이후 슬롯만 검사
            if (slot.isAfter(now)) {
                if (!blockedSlots.contains(slot)) {
                    return true; // 하나라도 비어있으면 예약 가능
                }
            }
        }

        return false; // 다 차 있으면 예약 불가
    }

}
