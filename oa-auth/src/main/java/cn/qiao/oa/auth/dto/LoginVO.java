package cn.qiao.oa.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private String token;

    private Long expiresIn;

    private UserInfoVO userInfo;
}
