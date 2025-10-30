package roombuddy.roombuddy.jpadomain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.Scope;
import roombuddy.roombuddy.jpadomain.baseentity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * 블랙 아웃
 */
@Entity
@Table(name = "blackout")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blackout extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blackout_id")
    private Long id;//PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;//스터디룸

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private Scope scope;//범위

    @Column(name = "reason", nullable = false)
    private String reason;//이유

    @Column(name = "start_at")
    private LocalDateTime startAt; //시작 시간

    @Column(name = "end_at")
    private LocalDateTime endAt; //종료 시간

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status",nullable = false)
    private ActiveStatus activeStatus; //활성화 상태


    /**
     * [생성 메서드]
     * @param scope 범위
     * @param reason 이유
     * @param startAt 시작 시간
     * @param endAt 종료 시간
     * @return Blackout
     */
    public static Blackout create(Scope scope, String reason,LocalDateTime startAt, LocalDateTime endAt) {
        Blackout blackout = new Blackout();
        blackout.scope = scope;
        blackout.reason = reason;
        blackout.startAt = startAt;
        blackout.endAt = endAt;
        blackout.activeStatus = ActiveStatus.ACTIVE;
        return blackout;
    }

    /**
     * [비즈니스 로직]
     * 스터디룸 세팅
     * @param room 스터디룸
     */
    public void assignToRoom(Room room) {
        this.room = room;
    }

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete() {
        this.activeStatus=ActiveStatus.INACTIVE;
    }

}
