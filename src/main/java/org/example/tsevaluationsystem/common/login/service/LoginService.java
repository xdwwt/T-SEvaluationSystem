package org.example.tsevaluationsystem.common.login.service;

import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;

public interface LoginService {
    //登录接口
   Result login(UserInfo user);

   /**
    * 修改密码
    * @param userId 当前登录用户账号
    * @param oldPassword 旧密码
    * @param newPassword 新密码
    * @return 结果
    */
   Result changePassword(String userId, String oldPassword, String newPassword);
}
