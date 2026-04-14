package org.example.tsevaluationsystem.common.login.controller;

import org.example.tsevaluationsystem.common.login.service.LoginService;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
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
}
