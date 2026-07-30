package cn.qiao.oa.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 50, message = "账号长度 3-50 位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度 6-100 位")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String realName;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;
}
