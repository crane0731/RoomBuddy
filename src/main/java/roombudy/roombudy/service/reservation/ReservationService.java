package roombudy.roombudy.service.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombudy.roombudy.dao.mapper.ReservationMapper;
import roombudy.roombudy.domain.Reservation;
import roombudy.roombudy.domain.Room;
import roombudy.roombudy.dto.api.PagedResponseDto;
import roombudy.roombudy.dto.member.MemberListResponseDto;
import roombudy.roombudy.dto.reservation.CreateReservationRequestDto;
import roombudy.roombudy.dto.reservation.MyReservationListResponseDto;
import roombudy.roombudy.dto.reservation.ReservationListResponseDto;
import roombudy.roombudy.dto.reservation.SearchReservationCondDto;
import roombudy.roombudy.exception.ErrorMessage;
import roombudy.roombudy.exception.ReservationCustomException;
import roombudy.roombudy.service.member.MemberService;
import roombudy.roombudy.service.room.RoomService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 서비스
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final MemberService memberService;//회원 서비스
    private final RoomService roomService;//스터디 룸 서비스
    private final ReservationMapper reservationMapper;//예약 매퍼


    /**
     * [서비스 로직]
     * 예약
     * @param roomId 스터디룸 아이디
     * @param dto 생성 요청 DTO
     */
    @Transactional
    public void createReservation(Long roomId,CreateReservationRequestDto dto){

        //회원 아이디 조회
        Long memberId = memberService.getLoginMemberId();

        //스터디룸 조회
        Room room = roomService.findById(roomId);

        LocalDateTime startAt = dto.getStartAt(); //예약 시작 시간
        LocalDateTime endAt   = dto.getEndAt(); //예약 종료 시간

        //원하는 예약 시간으로 해당 스터디룸에 예약이 이미 있는지 확인
        List<Reservation> conflicts = reservationMapper.findOverlappingReservationsForUpdate(roomId, startAt, endAt);
        if(!conflicts.isEmpty()){
            throw new ReservationCustomException(ErrorMessage.ALREADY_RESERVED);
        }

        Reservation reservation = Reservation.create(memberId, room.getRoomId(), startAt, endAt, dto.getDuration());

        save(reservation);

    }


    /**
     * [서비스 로직]
     * 예약 취소
     * @param reservationId 예약 아이디
     */
    @Transactional
    public void cancelReservation(Long reservationId){

        //현재 로그인한 회원의 PK 값
        Long memberId = memberService.getLoginMemberId();

        //예약 조회
        Reservation reservation = findById(reservationId);

        //취소 하려는 예약의 회원의 예약인지 확인
        validateReservationOwner(memberId, reservation);

        //예약 취소
        softDelete(reservation.getReservationId());
    }

    /**
     * [서비스 로직]
     * 특정 스터디룸의 당일 예약 목록 확인
     * @param roomId 스터디 룸 아이디
     * @return List<ReservationListResponseDto>
     */
    public List<ReservationListResponseDto> getReservationsByRoomAndDate(Long roomId){

        LocalDate today = LocalDate.now(); // 오늘 날짜 (yyyy-MM-dd)

        return reservationMapper.findReservationsByRoomAndDate(roomId, today);
    }


    /**
     * [서비스 로직]
     * 회원 자신의 예약 목록 확인 (현 시각 기준 이후 예약)
     * @return List<MyReservationListResponseDto>
     */
    public List<MyReservationListResponseDto> getMyReservation(){

        //현재 로그인한 회원의 PK 값
        Long memberId = memberService.getLoginMemberId();

        return reservationMapper.findMyReservationByMemberIdAndDate(memberId);
    }


    /**
     * [서비스 로직]
     * 스터디룸과 검색조건을 이용하여 예약 목록 조회
     * @param cond 검색 조건
     * @param roomId 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    public PagedResponseDto<ReservationListResponseDto> getReservationsByRoomAndCond(SearchReservationCondDto cond, Long roomId, int page){
        List<ReservationListResponseDto> content = reservationMapper.findAllReservationsByRoomAndCond(roomId, cond, page, 10);
        Long total = reservationMapper.countByRoomAndCond(roomId, cond);
        return createPagedResponseDto(content,total,page,10);

    }

    /**
     * [서비스 로직]
     * 스터디룸과 검색 조건을 이용하여 예약 목록 조회
     * @param roomId 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    public PagedResponseDto<ReservationListResponseDto> getConfirmedReservations(Long roomId,int page){
        List<ReservationListResponseDto> content = reservationMapper.findConfirmedReservationByRoom(roomId,page,10);
        Long total = reservationMapper.countByRoom(roomId);
        return createPagedResponseDto(content,total,page,10);
    }


    /**
     * [저장]
     * @param reservation 예약
     * @return int
     */
    @Transactional
    public int save(Reservation reservation){
        return reservationMapper.save(reservation);
    }

    /**
     * [조회]
     * PK 값 조회
     * @param reservationId 예약 아이디 PK
     * @return Reservation
     */
    public Reservation findById(Long reservationId){
        return reservationMapper.findById(reservationId).orElseThrow(()->new ReservationCustomException(ErrorMessage.NOT_FOUND_RESERVATION));
    }

    /**
     * [SOFT DELETE]
     * @param id PK
     */
    @Transactional
    public void softDelete(Long id){
        reservationMapper.softDelete(id);
    }

    //==취소 하려는 예약의 회원의 예약인지 확인==//
    private void validateReservationOwner(Long memberId, Reservation reservation) {
        if(!memberId.equals(reservation.getMemberId())){
            throw new ReservationCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<ReservationListResponseDto> createPagedResponseDto(List<ReservationListResponseDto> content, Long total, int page, int size) {

        int totalPages = (int) Math.ceil((double) total / size);
        boolean first = page <= 0;                   // 0번 페이지면 첫 페이지
        boolean last = (page + 1) >= totalPages;

        return PagedResponseDto.<ReservationListResponseDto>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(total)
                .first(first)
                .last(last)
                .build();
    }

}
