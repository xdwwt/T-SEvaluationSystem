package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;

public interface TeacherManagementService {
    /**
     * 新增教师
     * @param teacherInfo
     * @return
     */
    Result insert(TeacherInfo teacherInfo);

    /**
     * 查询教师列表
     * @param teacherInfo
     * @return
     */
    Result list(TeacherInfo teacherInfo);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    Result resetPassword(String userId);
}
