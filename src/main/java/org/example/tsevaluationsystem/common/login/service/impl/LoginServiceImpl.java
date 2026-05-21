package org.example.tsevaluationsystem.common.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.example.tsevaluationsystem.common.login.mapper.LoginMapper;
import org.example.tsevaluationsystem.common.login.service.LoginService;
import org.example.tsevaluationsystem.config.Jwt;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 登录业务实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    /**
     * 用户登录
     * @param user 用户信息（包含userId和password）
     * @return 登录结果（携带JWT Token）
     */
    @Override
    public Result login(UserInfo user) {
        UserInfo userInfo = loginMapper.selectUserLogin(user);
        if (userInfo == null || userInfo.getIsDele() == 1) {
            return new Result(0, "用户名或密码错误", null);
        }
        String jwt = Jwt.getJwt(userInfo);
        return new Result(1, "success", jwt);
    }

    /**
     * 修改用户密码
     * @param userId 用户账号
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @Override
    public Result changePassword(String userId, String oldPassword, String newPassword) {
        // 校验旧密码
        UserInfo query = new UserInfo();
        query.setUserId(userId);
        query.setPassword(oldPassword);
        UserInfo userInfo = loginMapper.selectUserLogin(query);
        if (userInfo == null) {
            return new Result(0, "旧密码错误", null);
        }

        // 更新密码
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userId)
               .set("password", newPassword);
        loginMapper.update(null, wrapper);
        return new Result(1, "密码修改成功，请重新登录", null);
    }
}
