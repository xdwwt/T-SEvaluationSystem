package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.Department;

@Mapper
public interface DepartmentManagementMapper extends BaseMapper<Department> {
}
