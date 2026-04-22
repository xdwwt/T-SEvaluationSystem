package org.example.tsevaluationsystem.common.login.service.impl;

import org.example.tsevaluationsystem.common.login.mapper.LoginMapper;
import org.example.tsevaluationsystem.common.login.service.LoginService;
import org.example.tsevaluationsystem.config.Jwt;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public Result login(UserInfo user) {
        UserInfo userInfo = loginMapper.selectUserLogin(user);
        if (userInfo == null || userInfo.getIsDele() == 1) {
            return new Result(0, "用户名或密码错误", null);
        }
        String jwt = Jwt.getJwt(userInfo);
        return new Result(1, "success", jwt);
    }
}
