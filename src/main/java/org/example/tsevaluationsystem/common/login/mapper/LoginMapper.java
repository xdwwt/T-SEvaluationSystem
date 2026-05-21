package org.example.tsevaluationsystem.common.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.UserInfo;

/**
 * 登录Mapper接口
 */
@Mapper
public interface LoginMapper extends BaseMapper<UserInfo> {
    UserInfo selectUserLogin(UserInfo user);
}
