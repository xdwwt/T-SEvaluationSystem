package org.example.tsevaluationsystem.common.login.service;

import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;

public interface LoginService {
    //登录接口
   Result login(UserInfo user);
}
