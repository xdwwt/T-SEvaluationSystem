package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Major;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

/**
 * 专业管理业务接口
 */
public interface MajorManagementService {

    /**
     * 新增专业
     * @param major 专业信息
     * @return 操作结果
     */
    Result insert(Major major);

    /**
     * 查询专业列表（分页）
     * @param params 查询参数
     * @return 专业列表
     */
    Result list(Map<String, Object> params);

    /**
     * 编辑专业
     * @param major 专业信息
     * @return 操作结果
     */
    Result update(Major major);

    /**
     * 删除专业（逻辑删除）
     * @param id 专业ID
     * @return 操作结果
     */
    Result delete(Long id);

    /**
     * 查询所有专业
     * @param params 可选查询参数
     * @return 专业列表
     */
    Result listAll(Map<String, Object> params);
}
