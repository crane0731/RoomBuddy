package roombuddy.roombuddy.jpadomain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roombuddy.roombuddy.enums.ActiveStatus;
import java.time.LocalTime;

/**
 * 스터디 룸
 */
@Entity
@Table(name="room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

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


}
