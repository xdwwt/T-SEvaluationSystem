package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.Department;

/**
 * 院系管理Mapper接口
 */
@Mapper
public interface DepartmentManagementMapper extends BaseMapper<Department> {
}
