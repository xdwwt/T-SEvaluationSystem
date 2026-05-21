package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.ClassTeacher;

/**
 * 排课管理Mapper接口
 */
@Mapper
public interface ArrangementManagementMapper extends BaseMapper<ClassTeacher> {
}
