package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.TitleManagementMapper;
import org.example.tsevaluationsystem.admin.service.TitleManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TitleManagementServiceImpl implements TitleManagementService {

    @Autowired
    private TitleManagementMapper titleManagementMapper;

    @Override
    public Result insert(Title title) {
        QueryWrapper<Title> wrapper = new QueryWrapper<>();
        wrapper.eq("title_name", title.getTitleName());
        if (titleManagementMapper.selectCount(wrapper) > 0) {
            return new Result(0, "职称名称已存在", null);
        }

        title.setId(IdWorker.getId());
        titleManagementMapper.insert(title);
        return new Result(1, "success", title.getId());
    }

    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<Title> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("titleName") != null && !params.get("titleName").toString().isEmpty()) {
            wrapper.like("title_name", params.get("titleName").toString());
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<Title> list = titleManagementMapper.selectList(wrapper);
        PageInfo<Title> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    @Override
    public Result listAll() {
        QueryWrapper<Title> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        wrapper.orderByAsc("title_name");
        List<Title> list = titleManagementMapper.selectList(wrapper);
        return new Result(1, "success", list);
    }

    @Override
    public Result update(Title title) {
        Title exist = titleManagementMapper.selectById(title.getId());
        if (exist == null) {
            return new Result(0, "职称不存在", null);
        }

        QueryWrapper<Title> wrapper = new QueryWrapper<>();
        wrapper.eq("title_name", title.getTitleName());
        wrapper.ne("id", title.getId());
        if (titleManagementMapper.selectCount(wrapper) > 0) {
            return new Result(0, "职称名称已存在", null);
        }

        titleManagementMapper.updateById(title);
        return new Result(1, "success", null);
    }

    @Override
    public Result delete(Long id) {
        Title exist = titleManagementMapper.selectById(id);
        if (exist == null) {
            return new Result(0, "职称不存在", null);
        }

        exist.setIsDele(1);
        titleManagementMapper.updateById(exist);
        return new Result(1, "success", null);
    }
}
