package cn.qiao.oa.auth.controller;

import cn.qiao.oa.auth.dto.*;
import cn.qiao.oa.auth.service.AuthService;
import cn.qiao.oa.common.core.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "登录、注册、登出、用户信息")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return R.ok(loginVO);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return R.ok();
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public R<Void> logout(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        authService.logout(token);
        return R.ok();
    }

    @GetMapping("/userinfo")
    @Operation(summary = "获取当前用户信息")
    public R<UserInfoVO> getUserInfo() {
        UserInfoVO userInfo = authService.getUserInfo();
        return R.ok(userInfo);
    }
}
