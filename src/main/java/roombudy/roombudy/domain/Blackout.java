package roombudy.roombudy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roombudy.roombudy.enums.ActiveStatus;
import roombudy.roombudy.enums.Scope;

import java.time.LocalDateTime;

/**
 * 블랙 아웃
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blackout {

    private Long blackoutId;//PK

    private Long roomId;//스터디룸 아이디

    private Scope scope;//범위

    private String reason;//이유

    private LocalDateTime startAt; //시작 시간

    private LocalDateTime endAt; //종료 시간

    private ActiveStatus activeStatus; //활성화 상태

    private LocalDateTime createdAt;//생성일

    private LocalDateTime updatedAt;//수정일

    /**
     * [생성 메서드]
     * @param scope 범위
     * @param reason 이유
     * @param startAt 시작 시간
     * @param endAt 종료 시간
     * @return Blackout
     */
    public static Blackout create( Scope scope, String reason,LocalDateTime startAt, LocalDateTime endAt) {
        Blackout blackout = new Blackout();
        blackout.setRoomId(null);
        blackout.setScope(scope);
        blackout.setReason(reason);
        blackout.setStartAt(startAt);
        blackout.setEndAt(endAt);
        blackout.setActiveStatus(ActiveStatus.ACTIVE);
        return blackout;

    }

    /**
     * [비즈니스 로직]
     * 스터디룸 세팅
     * @param roomId 룸 아이디
     */
    public void settingRoom(Long roomId) {
        this.roomId = roomId;
    }

}
