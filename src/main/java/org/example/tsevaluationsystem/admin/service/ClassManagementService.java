package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

/**
 * 班级管理业务接口
 */
public interface ClassManagementService {

    /**
     * 新增班级
     * @param classInfo 班级信息
     * @return 操作结果
     */
    Result insert(ClassInfo classInfo);

    /**
     * 查询班级列表（分页）
     * @param params 查询参数
     * @return 班级列表
     */
    Result list(Map<String, Object> params);

    /**
     * 编辑班级
     * @param classInfo 班级信息
     * @return 操作结果
     */
    Result update(ClassInfo classInfo);

    /**
     * 删除班级（逻辑删除）
     * @param id 班级ID
     * @return 操作结果
     */
    Result delete(Long id);

    /**
     * 查询所有班级
     * @return 班级列表
     */
    Result listAll();

    /**
     * 查询班级学生列表
     * @param classId 班级ID
     * @return 学生列表
     */
    Result listClassStudents(Long classId);

    /**
     * 查询未分配班级的学生
     * @return 未分配学生列表
     */
    Result listUnassignedStudents();

    /**
     * 将学生加入班级
     * @param classId 班级ID
     * @param studentId 学生ID
     * @return 操作结果
     */
    Result addStudentToClass(Long classId, Long studentId);

    /**
     * 将学生移出班级
     * @param id 班级学生关联ID
     * @return 操作结果
     */
    Result removeStudentFromClass(Long id);
}
