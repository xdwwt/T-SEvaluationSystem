package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.TitleManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/title")
public class TitleManagementController {

    @Autowired
    private TitleManagementService titleManagementService;

    @PostMapping("/add")
    public Result add(@RequestBody Title title) {
        if (title.getTitleName() == null || title.getTitleName().trim().isEmpty()) {
            return new Result(0, "职称名称不能为空", null);
        }
        title.setTitleName(title.getTitleName().trim());
        return titleManagementService.insert(title);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return titleManagementService.list(params);
    }

    @PostMapping("/listAll")
    public Result listAll() {
        return titleManagementService.listAll();
    }

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

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return titleManagementService.delete(id);
    }
}
