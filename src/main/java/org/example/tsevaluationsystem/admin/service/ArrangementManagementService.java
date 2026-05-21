package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.ClassTeacher;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

/**
 * 排课管理业务接口
 */
public interface ArrangementManagementService {

    /**
     * 新增排课记录
     * @param classTeacher 排课信息
     * @return 操作结果
     */
    Result insert(ClassTeacher classTeacher);

    /**
     * 查询排课列表（分页）
     * @param params 查询参数
     * @return 排课列表
     */
    Result list(Map<String, Object> params);

    /**
     * 编辑排课记录
     * @param classTeacher 排课信息
     * @return 操作结果
     */
    Result update(ClassTeacher classTeacher);

    /**
     * 删除排课记录（逻辑删除）
     * @param id 排课ID
     * @return 操作结果
     */
    Result delete(Long id);
}
