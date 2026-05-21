package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.TitleManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 职称管理控制器
 * <p>负责职称信息的增删改查</p>
 */
@RestController
@RequestMapping("/admin/title")
public class TitleManagementController {

    @Autowired
    private TitleManagementService titleManagementService;

    /**
     * 新增职称
     * @param title 职称信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody Title title) {
        if (title.getTitleName() == null || title.getTitleName().trim().isEmpty()) {
            return new Result(0, "职称名称不能为空", null);
        }
        title.setTitleName(title.getTitleName().trim());
        return titleManagementService.insert(title);
    }

    /**
     * 查询职称列表（分页）
     * @param params 查询参数
     * @return 职称列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return titleManagementService.list(params);
    }

    /**
     * 查询所有职称
     * @return 职称列表
     */
    @PostMapping("/listAll")
    public Result listAll() {
        return titleManagementService.listAll();
    }

    /**
     * 编辑职称
     * @param title 职称信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Title title) {
        if (title.getId() == null) {
            return new Result(0, "职称ID不能为空", null);
        }
        if (title.getTitleName() == null || title.getTitleName().trim().isEmpty()) {
            return new Result(0, "职称名称不能为空", null);
        }
        title.setTitleName(title.getTitleName().trim());
        return titleManagementService.update(title);
    }

    /**
     * 删除职称（逻辑删除）
     * @param id 职称ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return titleManagementService.delete(id);
    }
}
