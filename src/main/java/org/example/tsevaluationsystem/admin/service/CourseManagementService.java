package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Course;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

/**
 * 课程管理业务接口
 */
public interface CourseManagementService {

    /**
     * 新增课程
     * @param course 课程信息
     * @return 操作结果
     */
    Result insert(Course course);

    /**
     * 查询课程列表（分页）
     * @param params 查询参数
     * @return 课程列表
     */
    Result list(Map<String, Object> params);

    /**
     * 编辑课程
     * @param course 课程信息
     * @return 操作结果
     */
    Result update(Course course);

    /**
     * 删除课程（逻辑删除）
     * @param id 课程ID
     * @return 操作结果
     */
    Result delete(Long id);

    /**
     * 查询所有课程
     * @return 课程列表
     */
    Result listAll();
}
