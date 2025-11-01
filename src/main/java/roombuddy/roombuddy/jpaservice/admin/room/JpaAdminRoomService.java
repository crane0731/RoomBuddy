package roombuddy.roombuddy.jpaservice.admin.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dto.room.CreateRoomRequestDto;
import roombuddy.roombuddy.dto.room.UpdateRoomRequestDto;
import roombuddy.roombuddy.jpadomain.Room;
import roombuddy.roombuddy.jpaservice.room.JpaRoomService;

/**
 * 관리자 스터디룸 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaAdminRoomService {

    private final JpaRoomService roomService;//스터디룸 서비스



    /**
     * [서비스 로직]
     * 스터디룸 생성
     * @param dto 생성 요청 DTO
     */
    @Transactional
    public void createRoom(CreateRoomRequestDto dto) {

        //Room 도메인 객체 생성
        Room room = Room.create(dto.getName(), dto.getCapacity(),dto.getOpenTime(),dto.getCloseTime());

        //저장
        roomService.save(room);
    }


    /**
     * [서비스 로직]
     * 스터디룸 삭제
     * @param roomId 스터디룸 아이디
     */
    @Transactional
    public void deleteRoom(Long roomId) {

        //스터디룸 조회
        Room room = roomService.findById(roomId);

        //SOFT DELETE
        room.softDelete();
    }

    /**
     * [서비스 로직]
     * 스터디룸 정보 수정
     * @param roomId 룸 아이디
     * @param dto 수정 요청 DTO
     */
    @Transactional
    public void updateRoom(Long roomId, UpdateRoomRequestDto dto) {
        roomService.update(roomId, dto);
    }



}
