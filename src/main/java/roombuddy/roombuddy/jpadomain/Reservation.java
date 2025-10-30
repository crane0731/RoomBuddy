package roombuddy.roombuddy.jpadomain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.ReservationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;//PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //회원 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;//스터디룸 아이디

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;//예약 상태

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt; //시작 시간

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt; //종료 시간

    @Column(name = "duration", nullable = false)
    private Long duration; //예약 기간

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus; //활성화 상태

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        this.status=ReservationStatus.CANCELED;
        this.activeStatus=ActiveStatus.INACTIVE;
    }

}
