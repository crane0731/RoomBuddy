package roombudy.roombudy.dto.reservation;

import lombok.Getter;
import lombok.Setter;
import roombudy.roombudy.enums.CreatedType;
import roombudy.roombudy.enums.ReservationStatus;

import java.time.LocalDate;

/**
 * 예약 목록 검색 조건 DTO
 */
@Getter
@Setter
public class SearchReservationCondDto {

    private String memberEmail;//회원 이메일

    private ReservationStatus status;//예약 상태

    private LocalDate date; //날짜

    private CreatedType created; //생성일 기준 타입


    /**
     * [생성 메서드]
     * @param memberEmail 회원 이메일
     * @param status 예약 상태
     * @param created 생성일 기준
     * @param date 날짜
     * @return SearchReservationCondDto
     */
    public static SearchReservationCondDto create(String memberEmail, ReservationStatus status, LocalDate date,CreatedType created) {

        SearchReservationCondDto dto = new SearchReservationCondDto();
        dto.memberEmail = memberEmail;
        dto.status = status;
        dto.date = date;
        dto.created = created;
        return dto;
    }





}
