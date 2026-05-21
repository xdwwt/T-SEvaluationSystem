package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tsevaluationsystem.dto.Course;

/**
 * 课程管理Mapper接口
 */
@Mapper
public interface CourseManagementMapper extends BaseMapper<Course> {
}
