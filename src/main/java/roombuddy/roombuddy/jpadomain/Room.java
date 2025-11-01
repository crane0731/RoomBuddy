package roombuddy.roombuddy.jpadomain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.jpadomain.baseentity.BaseTimeEntity;

import java.time.LocalTime;

/**
 * 스터디 룸
 */
@Entity
@Table(name="room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;//PK

    @Column(name = "name", nullable = false)
    private String name; //이름

    @Column(name = "capacity", nullable = false)
    private Long capacity;//수용 인원

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus; //활성화 상태

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;  // 예약 가능 시작시간

    @Column(name = "close_time",nullable = false)
    private LocalTime closeTime; // 예약 가능 종료시간

    /**
     * [생성 메서드]
     * @param name 이름
     * @param capacity 수용 인원
     * @param openTime 예약 가능 시작 시간
     * @param closeTime 예약 가능 종료 시간
     * @return Room
     */
    public static Room create(String name , Long capacity, LocalTime openTime, LocalTime closeTime) {
        Room room = new Room();
        room.name = name;
        room.capacity = capacity;
        room.openTime = openTime;
        room.closeTime = closeTime;
        room.activeStatus = ActiveStatus.ACTIVE;
        return room;
    }

    /**
     * [비즈니스 로직]
     * 업데이트
     * @param name 이름
     * @param capacity 수용 인원
     * @param openTime 오픈 시간
     * @param closeTime 종료 시간
     */
    public void update(String name, Long capacity, LocalTime openTime, LocalTime closeTime) {
        this.name = name;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        this.activeStatus = ActiveStatus.INACTIVE;
    }


}
