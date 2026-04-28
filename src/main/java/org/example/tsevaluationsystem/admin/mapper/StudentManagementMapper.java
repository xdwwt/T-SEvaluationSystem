package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.StudentInfo;

@Mapper
public interface StudentManagementMapper extends BaseMapper<StudentInfo> {
}
