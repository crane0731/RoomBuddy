package roombuddy.roombuddy.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 전화번호 수정 요청 DTO
 */
@Getter
@Setter
public class UpdateMemberPhoneRequestDto {

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;
}
