package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

/**
 * 院系管理业务接口
 */
public interface DepartmentManagementService {

    /**
     * 新增院系
     * @param department 院系信息
     * @return 操作结果
     */
    Result insert(Department department);

    /**
     * 查询院系列表（分页）
     * @param params 查询参数
     * @return 院系列表
     */
    Result list(Map<String, Object> params);

    /**
     * 查询所有院系
     * @return 院系列表
     */
    Result listAll();

    /**
     * 编辑院系
     * @param department 院系信息
     * @return 操作结果
     */
    Result update(Department department);

    /**
     * 删除院系（逻辑删除）
     * @param id 院系ID
     * @return 操作结果
     */
    Result delete(Long id);
}
