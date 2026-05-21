package org.example.tsevaluationsystem.common.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.tsevaluationsystem.common.login.service.LoginService;
import org.example.tsevaluationsystem.config.JwtUtil;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录与密码相关接口
 */
@RestController
@RequestMapping("/main")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     * @param userId 登录账号
     * @param password 密码
     * @return JWT Token
     */
    @PostMapping("/login")
    public Result login(@RequestParam String userId, @RequestParam String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setPassword(password);
        return loginService.login(userInfo);
    }

    /**
     * 修改当前登录用户密码
     * @param request HTTP请求
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @PostMapping("/password/change")
    public Result changePassword(HttpServletRequest request,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {
        String userId = JwtUtil.getCurrentUserId(request);
        if (userId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return new Result(0, "新密码不能为空", null);
        }
        return loginService.changePassword(userId, oldPassword, newPassword);
    }
}
