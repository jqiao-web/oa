package cn.qiao.oa.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long userId;

    private String username;

    private String realName;

    private String avatar;

    private Long deptId;

    private String deptName;

    private List<String> roles;

    private List<String> permissions;

    private List<MenuVO> menus;
}
