package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;

import java.util.Map;

public interface TeacherManagementService {
    /**
     * 新增教师
     * @param teacherInfo
     * @return
     */
    Result insert(TeacherInfo teacherInfo);

    /**
     * 查询教师列表（分页）
     * @param params
     * @return
     */
    Result list(Map<String, Object> params);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    Result resetPassword(String userId);

    /**
     * 删除教师（逻辑删除）
     * @param userId
     * @return
     */
    Result delete(String userId);

    /**
     * 编辑教师
     * @param teacherInfo 教师信息
     * @return 操作结果
     */
    Result update(TeacherInfo teacherInfo);

    /**
     * 查询所有教师
     * @return 教师列表
     */
    Result listAll();
}
