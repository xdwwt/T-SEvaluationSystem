package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.StudentInfo;

import java.util.Map;

public interface StudentManagementService {
    /**
     * 新增学生
     * @param studentInfo
     * @return
     */
    Result insert(StudentInfo studentInfo);

    /**
     * 查询学生列表（分页）
     * @param params
     * @return
     */
    Result list(Map<String, Object> params);

    /**
     * 编辑学生
     * @param studentInfo
     * @return
     */
    Result update(StudentInfo studentInfo);

    /**
     * 删除学生（逻辑删除）
     * @param userId
     * @return
     */
    Result delete(String userId);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    Result resetPassword(String userId);
}
