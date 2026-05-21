package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Title;

import java.util.Map;

/**
 * 职称管理业务接口
 */
public interface TitleManagementService {

    /**
     * 新增职称
     * @param title 职称信息
     * @return 操作结果
     */
    Result insert(Title title);

    /**
     * 查询职称列表（分页）
     * @param params 查询参数
     * @return 职称列表
     */
    Result list(Map<String, Object> params);

    /**
     * 查询所有职称
     * @return 职称列表
     */
    Result listAll();

    /**
     * 编辑职称
     * @param title 职称信息
     * @return 操作结果
     */
    Result update(Title title);

    /**
     * 删除职称（逻辑删除）
     * @param id 职称ID
     * @return 操作结果
     */
    Result delete(Long id);
}
