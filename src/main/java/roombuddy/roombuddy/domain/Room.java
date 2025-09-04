package roombuddy.roombuddy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roombuddy.roombuddy.enums.ActiveStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 스터디 룸
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {


    private Long roomId;//PK

    private String name; //이름

    private Long capacity;//수용 인원

    private ActiveStatus activeStatus; //활성화 상태

    private LocalTime openTime;  // 예약 가능 시작시간

    private LocalTime closeTime; // 예약 가능 종료시간


    private LocalDateTime createdAt;//생성일

    private LocalDateTime updatedAt;//수정일

    /**
     * [생성 메서드]
     * @param name 이름
     * @param capacity 수용 인원
     * @return Room
     */
    public static Room create(String name ,Long capacity,java.time.LocalTime openTime,java.time.LocalTime closeTime){
        Room room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        room.setActiveStatus(ActiveStatus.ACTIVE);
        room.setOpenTime(openTime);
        room.setCloseTime(closeTime);
        return room;
    }
}
