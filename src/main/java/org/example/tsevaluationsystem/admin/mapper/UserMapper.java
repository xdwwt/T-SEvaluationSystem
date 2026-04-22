package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.UserInfo;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
