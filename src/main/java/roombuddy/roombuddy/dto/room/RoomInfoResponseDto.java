package roombuddy.roombuddy.dto.room;

import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.jpadomain.Room;
import roombuddy.roombuddy.utils.DateFormatUtil;

import java.time.LocalTime;

/**
 * 스터디룸 정보 응답 DTO
 */
@Getter
@Setter
public class RoomInfoResponseDto {

    private Long roomId; //룸 아이디
    private String name; //이름
    private Long capacity; //수용인원

    private LocalTime openTime; //운영 시작 시간
    private LocalTime closeTime; //운영 종료 시간

    private String createdAt; //생성일
    private String updatedAt; //수정일

    /**
     * [생성 메서드]
     * @param room 스터디룸
     * @return RoomInfoResponseDto
     */
    public static RoomInfoResponseDto create(Room room) {
        RoomInfoResponseDto dto = new RoomInfoResponseDto();
        dto.setRoomId(room.getId());
        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());

        dto.setOpenTime(room.getOpenTime());
        dto.setCloseTime(room.getCloseTime());

        dto.setCreatedAt(DateFormatUtil.DateFormat(room.getCreatedAt()));
        dto.setUpdatedAt(DateFormatUtil.DateFormat(room.getUpdatedAt()));
        return dto;
    }

}
