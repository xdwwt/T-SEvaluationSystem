package org.example.tsevaluationsystem.common.login.service;

import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;

/**
 * 登录业务接口
 */
public interface LoginService {

   /**
    * 用户登录
    * @param user 用户信息（包含userId和password）
    * @return 登录结果（携带JWT Token）
    */
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
