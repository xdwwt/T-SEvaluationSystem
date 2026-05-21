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

@RestController
@RequestMapping("/main")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestParam String userId, @RequestParam String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setPassword(password);
        return loginService.login(userInfo);
    }

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
