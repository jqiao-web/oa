package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.dto.LoginDTO;
import cn.qiao.oa.auth.dto.LoginVO;
import cn.qiao.oa.auth.dto.RegisterDTO;
import cn.qiao.oa.auth.dto.UserInfoVO;

public interface AuthService {

    LoginVO login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    void logout(String token);

    UserInfoVO getUserInfo();
}
