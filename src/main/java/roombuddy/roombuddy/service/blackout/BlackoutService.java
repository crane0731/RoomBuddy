package roombuddy.roombuddy.service.blackout;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dao.mapper.BlackoutMapper;
import roombuddy.roombuddy.dao.mapper.ReservationMapper;
import roombuddy.roombuddy.domain.Blackout;
import roombuddy.roombuddy.domain.Reservation;
import roombuddy.roombuddy.domain.Room;
import roombuddy.roombuddy.dto.api.PagedResponseDto;
import roombuddy.roombuddy.dto.blackout.BlackoutDto;
import roombuddy.roombuddy.dto.blackout.BlackoutListResponseDto;
import roombuddy.roombuddy.dto.blackout.CreateBlackoutRequestDto;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.Scope;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.RoomCustomException;
import roombuddy.roombuddy.service.room.RoomService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 블랙 아웃 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackoutService {

    private final RoomService roomService;//스터디룸 서비스

    private final BlackoutMapper blackoutMapper;//블랙 아웃 매퍼
    private final ReservationMapper reservationMapper; //예약 매퍼



    /**
     * [서비스 로직]
     * 관리자 - 블랙 아웃 등록
     * @param dto 생성 요청 DTO
     */
    @Transactional
    public void createBlackout(CreateBlackoutRequestDto dto){

        //블랙 아웃 도메인 객체 생성
        Blackout blackout = Blackout.create( dto.getScope(), dto.getReason(), dto.getStartAt(), dto.getEndAt());

        //만약 특정 스터디룸에 대한 블랙 아웃일 경우
        if(dto.getRoomId()!=null && dto.getScope().equals(Scope.ROOM)){

            //스터디룸 조회
            Room room = roomService.findById(dto.getRoomId());

            //스터디룸이 존재하는지 검증
            validateExistsRoom(room);

            blackout.settingRoom(room.getRoomId());

        }

        //블랙 아웃 시간에 이미 진행중인 예약이 있는지 확인 하고 모두 삭제 처리(SOFT DELETE)
        List<Reservation> overlappingReservations = reservationMapper.findOverlappingReservations(dto.getStartAt(), dto.getEndAt());
        for (Reservation overlappingReservation : overlappingReservations) {
            reservationMapper.save(overlappingReservation);
        }

        //블랙 아웃 저장
        save(blackout);
    }



    /**
     * [서비스 로직]
     * 관리자 - 블랙 아웃 제거
     * @param blackoutId 블랙 아웃 아이디
     */
    @Transactional
    public void deleteBlackout(Long blackoutId){
        blackoutMapper.softDelete(blackoutId);
    }


    /**
     * [서비스 로직]
     * * 관리자 - 현재 진행중인& 예정인 블랙 아웃 목록 조회
     * @param page 페이지 번호
     * @return PagedResponseDto<BlackoutListResponseDto>
     */
    public PagedResponseDto<BlackoutListResponseDto> getActiveBlackoutsByRoom(int page){

        List<BlackoutListResponseDto> content = blackoutMapper.findActiveBlackouts(page, 10);

        Long total = blackoutMapper.countActiveBlackouts();

        return createPagedResponseDto(content,total,page,10);
    }

    /**
     * [서비스 로직]
     * 관리자 - 특정 스터디룸의 블랙아웃 목록 조회
     * @param roomId 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<BlackoutListResponseDto>
     */
    public PagedResponseDto<BlackoutListResponseDto> getBlackoutsByRoom(Long roomId,int page){

        List<BlackoutListResponseDto> content = blackoutMapper.findAllByRoomId(roomId,page, 10);

        Long total = blackoutMapper.countByRoomId(roomId);

        return createPagedResponseDto(content,total,page,10);
    }



    /**
     * [서비스 로직]
     * 특정 스터디룸의 오늘 블랙 아웃 목록 조회
     * @param roomId 스터디룸 아이디
     * @return List<BlackoutDto>
     */
    public List<BlackoutDto> getTodayBlackoutsByRoom(Long roomId){
        LocalDate today = LocalDate.now();

        return blackoutMapper.findBlackoutsByDate(roomId,today);
    }


    /**
     * [서비스 로직]
     * 주어진 시간과 겹치는 블랙 아웃 목록 조회
     * @param roomId 스터디룸 아이디
     * @param startAt 시작 시각
     * @param endAt 종료 시각
     * @return List<Blackout>
     */
    public List<Blackout> getOverlappingBlackouts(Long roomId, LocalDateTime startAt, LocalDateTime endAt){
        return blackoutMapper.findOverlappingBlackouts(roomId, startAt, endAt);
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<BlackoutListResponseDto> createPagedResponseDto(List<BlackoutListResponseDto> content, Long total, int page, int size) {

        int totalPages = (int) Math.ceil((double) total / size);
        boolean first = page <= 0;                   // 0번 페이지면 첫 페이지
        boolean last = (page + 1) >= totalPages;

        return PagedResponseDto.<BlackoutListResponseDto>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(total)
                .first(first)
                .last(last)
                .build();
    }


    /**
     * [저장]
     * @param blackout 블랙 아웃
     * @return int
     */
    @Transactional
    public int save(Blackout blackout) {
        return blackoutMapper.save(blackout);
    }

    //==스터디룸이 존재하는지 검증==//
    private void validateExistsRoom(Room room) {
        if(room.getActiveStatus().equals(ActiveStatus.INACTIVE)){
            throw new RoomCustomException(ErrorMessage.ALREADY_DELETED_ROOM);
        }
    }


}
