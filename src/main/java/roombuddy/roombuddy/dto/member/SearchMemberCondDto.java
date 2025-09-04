package roombuddy.roombuddy.dto.member;

import lombok.Getter;
import lombok.Setter;
import roombuddy.roombuddy.enums.MemberSortType;

/**
 * 회원 검색 조건 DTO
 */
@Getter
@Setter
public class SearchMemberCondDto {


    private String name; //이름
    private String email; //이메일

    private MemberSortType sortType;//정렬 타입

    /**
     * [생성 메서드]
     * @param name 이름
     * @param email 이메일
     * @param sortType 정렬 타입
     * @return SearchMemberCondDto
     */
    public static SearchMemberCondDto create(String name, String email, MemberSortType sortType) {
        SearchMemberCondDto dto = new SearchMemberCondDto();
        dto.name = name;
        dto.email = email;
        dto.sortType = sortType;
        return dto;
    }
}
