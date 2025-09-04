package roombuddy.roombuddy.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 회원 가입 요청 DTO
 */
@Getter
public class SignUpRequestDto {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    private String passwordCheck;
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone;

}
