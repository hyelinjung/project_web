package shoppingMall.project.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import shoppingMall.project.constant.Role;


//회원가입 DTO
@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "필수 입력 값 입니다") //아이디 값
    private String username;
    @NotBlank(message = "필수 입력 값 입니다")
    @Length(min = 4,max = 6, message = "비밀번호는 4자 이상 6자 이하로 작성해주세요")
    private String password;

    private String addr; //도로명
    private String addr1; //주소
    @NotBlank(message = "필수 입력 값 입니다")
    private String  addr2; //상세주소

    @NotBlank(message = "필수 입력 값 입니다")
    @Email(message = "형식이 틀립니다")
    private String email;

}
