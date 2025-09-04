package roombudy.roombudy.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 이름 수정 요청 DTO
 */
@Getter
@Setter
public class UpdateMemberNameRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name; //이름

}
