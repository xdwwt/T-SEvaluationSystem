package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.Major;

/**
 * 专业管理Mapper接口
 */
@Mapper
public interface MajorManagementMapper extends BaseMapper<Major> {
}
