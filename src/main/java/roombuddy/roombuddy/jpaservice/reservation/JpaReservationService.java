package roombuddy.roombuddy.jpaservice.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dto.api.PagedResponseDto;
import roombuddy.roombuddy.dto.reservation.CreateReservationRequestDto;
import roombuddy.roombuddy.dto.reservation.MyReservationListResponseDto;
import roombuddy.roombuddy.dto.reservation.ReservationListResponseDto;
import roombuddy.roombuddy.dto.reservation.SearchReservationCondDto;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.ReservationCustomException;
import roombuddy.roombuddy.jpadomain.Blackout;
import roombuddy.roombuddy.jpadomain.Member;
import roombuddy.roombuddy.jpadomain.Reservation;
import roombuddy.roombuddy.jpadomain.Room;
import roombuddy.roombuddy.jpaservice.blackout.JpaBlackoutService;
import roombuddy.roombuddy.jpaservice.member.JpaMemberService;
import roombuddy.roombuddy.jpaservice.room.JpaRoomService;
import roombuddy.roombuddy.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA 예약 서비스
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaReservationService {

    private final JpaMemberService memberService; //회원 서비스
    private final JpaRoomService roomService;//스터디 룸 서비스
    private final JpaBlackoutService blackoutService;//블랙 아웃 서비스
    private final ReservationRepository reservationRepository;//예약 레파지토리


    /**
     * [서비스 로직]
     * 예약
     * @param roomId 스터디룸 아이디
     * @param dto 생성 요청 DTO
     */
    @Transactional
    public void createReservation(Long roomId,CreateReservationRequestDto dto){

        //현재 로그인한 회원 조회
        Member member = memberService.getLoginMember();

        //스터디룸 조회
        Room room = roomService.findById(roomId);

        LocalDateTime startAt = dto.getStartAt(); //예약 시작 시간
        LocalDateTime endAt   = dto.getEndAt(); //예약 종료 시간

        //원하는 예약 시간으로 해당 스터디룸에 예약이 이미 있는지 확인
        List<Reservation> conflicts = reservationRepository.findOverlappingReservationsByRoomIdForUpdate(roomId, startAt, endAt);
        if(!conflicts.isEmpty()){
            throw new ReservationCustomException(ErrorMessage.ALREADY_RESERVED);
        }

        //블랙 아웃 확인
        List<Blackout> blackouts = blackoutService.getOverlappingBlackouts(roomId, startAt, endAt);
        if (!blackouts.isEmpty()) {
            throw new ReservationCustomException(ErrorMessage.BLACKOUT_TIME);
        }

        Reservation reservation = Reservation.create(member, room, startAt, endAt, dto.getDuration());

        save(reservation);

    }


    /**
     * [서비스 로직]
     * 예약 취소
     * @param reservationId 예약 아이디
     */
    @Transactional
    public void cancelReservation(Long reservationId){

        //현재 로그인한 회원
        Member member = memberService.getLoginMember();

        //예약 조회
        Reservation reservation = findById(reservationId);

        //취소 하려는 예약의 회원의 예약인지 확인
        validateReservationOwner(member.getId(), reservation);

        //예약 취소
        reservation.softDelete();
    }

    /**
     * [서비스 로직]
     * 특정 스터디룸의 당일 예약 목록 확인
     * @param roomId 스터디 룸 아이디
     * @return List<ReservationListResponseDto>
     */
    public List<ReservationListResponseDto> getReservationsByRoomAndDate(Long roomId){

        LocalDate today = LocalDate.now(); // 오늘 날짜 (yyyy-MM-dd)

        return reservationRepository.findReservationsByRoomAndDate(roomId, today).stream().map(ReservationListResponseDto::create).toList();

    }


    /**
     * [서비스 로직]
     * 회원 자신의 예약 목록 확인 (현 시각 기준 이후 예약)
     * @return List<MyReservationListResponseDto>
     */
    public List<MyReservationListResponseDto> getMyReservation(){

        //현재 로그인한 회원
        Member member = memberService.getLoginMember();

        //조회 + 응답 DTO 생성 + 반환
        return reservationRepository.findMyReservationByMemberIdAndDate(member.getId()).stream().map(MyReservationListResponseDto::create).toList();
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

        Page<Reservation> pageResult = reservationRepository.findAllReservationsByRoomAndCond(roomId, cond, PageRequest.of(page, 10));
        List<ReservationListResponseDto> content = pageResult.getContent().stream().map(ReservationListResponseDto::create).toList();


        return createPagedResponseDto(content,pageResult);
    }

    /**
     * [서비스 로직]
     * 특정스터디룸의 예약완료된 예약 로그 목록 조회
     * @param roomId 스터디룸 아이디
     * @param page 페이지 번호
     * @return PagedResponseDto<ReservationListResponseDto>
     */
    public PagedResponseDto<ReservationListResponseDto> getConfirmedReservations(Long roomId,int page){

        Page<Reservation> pageResult = reservationRepository.findConfirmedReservationByRoom(roomId, PageRequest.of(page, 10));
        List<ReservationListResponseDto> content = pageResult.getContent().stream().map(ReservationListResponseDto::create).toList();


        return createPagedResponseDto(content,pageResult);
    }


    /**
     * [저장]
     * @param reservation 예약
     */
    @Transactional
    public void save(Reservation reservation){
         reservationRepository.save(reservation);
    }

    /**
     * [조회]
     * PK 값 조회
     * @param reservationId 예약 아이디 PK
     * @return Reservation
     */
    public Reservation findById(Long reservationId){
        return reservationRepository.findById(reservationId).orElseThrow(()->new ReservationCustomException(ErrorMessage.NOT_FOUND_RESERVATION));
    }



    //==취소 하려는 예약의 회원의 예약인지 확인==//
    private void validateReservationOwner(Long memberId, Reservation reservation) {
        if(!memberId.equals(reservation.getMember().getId())){
            throw new ReservationCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<ReservationListResponseDto> createPagedResponseDto(List<ReservationListResponseDto> content, Page<Reservation> page) {


        return PagedResponseDto.<ReservationListResponseDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

}
