package roombuddy.roombuddy.jpaservice.blackout;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roombuddy.roombuddy.dto.api.PagedResponseDto;
import roombuddy.roombuddy.dto.blackout.BlackoutDto;
import roombuddy.roombuddy.dto.blackout.BlackoutListResponseDto;
import roombuddy.roombuddy.dto.blackout.CreateBlackoutRequestDto;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.Scope;
import roombuddy.roombuddy.exception.BlackoutCustomException;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.RoomCustomException;
import roombuddy.roombuddy.jpadomain.Blackout;
import roombuddy.roombuddy.jpadomain.Reservation;
import roombuddy.roombuddy.jpadomain.Room;
import roombuddy.roombuddy.jpaservice.room.JpaRoomService;
import roombuddy.roombuddy.repository.BlackoutRepository;
import roombuddy.roombuddy.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA 블랙 아웃 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaBlackoutService {

    private final JpaRoomService roomService;//스터디룸 서비스
    private final BlackoutRepository blackoutRepository;//블랙아웃 레파지토리
    private final ReservationRepository reservationRepository; //예약 레파지토리


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

            //존재 한다면 스터디룸 배정
            blackout.assignToRoom(room);

        }

        //블랙 아웃 시간에 이미 진행중인 예약이 있는지 확인 하고 모두 삭제 처리(SOFT DELETE)
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(dto.getStartAt(), dto.getEndAt());
        for (Reservation overlappingReservation : overlappingReservations) {
            overlappingReservation.softDelete();
        }

        //블랙 아웃 저장
        save(blackout);
    }


    /**
     * [서비스 로직]
     * * 관리자 - 현재 진행중인& 예정인 블랙 아웃 목록 조회
     * @param page 페이지 번호
     * @return PagedResponseDto<BlackoutListResponseDto>
     */
    public PagedResponseDto<BlackoutListResponseDto> getActiveBlackoutsByRoom(int page){

        Page<Blackout> pageResult = blackoutRepository.findActiveBlackouts(PageRequest.of(page, 10));

        List<BlackoutListResponseDto> content = pageResult.getContent().stream().map(BlackoutListResponseDto::create).toList();

        return createPagedResponseDto(content,pageResult);
    }

    /**
     * [서비스 로직]
     * 관리자 - 특정 스터디룸의 블랙아웃 목록 조회
     * @param roomId 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<BlackoutListResponseDto>
     */
    public PagedResponseDto<BlackoutListResponseDto> getBlackoutsByRoom(Long roomId,int page){

        Page<Blackout> pageResult = blackoutRepository.findAllByRoomId(roomId, PageRequest.of(page, 10));

        List<BlackoutListResponseDto> content = pageResult.getContent().stream().map(BlackoutListResponseDto::create).toList();

        return createPagedResponseDto(content,pageResult);
    }



    /**
     * [서비스 로직]
     * 특정 스터디룸의 오늘 블랙 아웃 목록 조회
     * @param roomId 스터디룸 아이디
     * @return List<BlackoutDto>
     */
    public List<BlackoutDto> getTodayBlackoutsByRoom(Long roomId){
        LocalDate today = LocalDate.now();

        return blackoutRepository.findBlackoutsByDate(roomId,today);

    }
    /**
     * [서비스 로직]
     * 관리자 - 블랙 아웃 제거
     * @param blackoutId 블랙 아웃 아이디
     */
    @Transactional
    public void deleteBlackout(Long blackoutId){
        Blackout blackout = blackoutRepository.findById(blackoutId).orElseThrow(() -> new BlackoutCustomException(ErrorMessage.NOT_FOUND_BLACKOUT));
        blackout.softDelete();
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
        return blackoutRepository.findOverlappingBlackouts(roomId, startAt, endAt);
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<BlackoutListResponseDto> createPagedResponseDto(List<BlackoutListResponseDto> content, Page<Blackout> page) {


        return PagedResponseDto.<BlackoutListResponseDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }





    /**
     * [저장]
     * @param blackout 블랙 아웃
     */
    @Transactional
    public void save(Blackout blackout) {
         blackoutRepository.save(blackout);
    }

    //==스터디룸이 존재하는지 검증==//
    private void validateExistsRoom(Room room) {
        if(room.getActiveStatus().equals(ActiveStatus.INACTIVE)){
            throw new RoomCustomException(ErrorMessage.ALREADY_DELETED_ROOM);
        }
    }


}
