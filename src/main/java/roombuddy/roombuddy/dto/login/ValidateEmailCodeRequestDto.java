package roombudy.roombudy.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 인증 코드 요청 DTO
 */
@Getter
@Setter
public class ValidateEmailCodeRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email; //이메일

    @NotBlank(message = "코드를 입력해주세요.")
    private String code; //인증코드

}
