package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.example.tsevaluationsystem.admin.mapper.TeacherManagementMapper;
import org.example.tsevaluationsystem.admin.service.TeacherManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherManagementServiceImpl implements TeacherManagementService {

    @Autowired
    private TeacherManagementMapper teacherManagementMapper;

    @Override
    public Result insert(TeacherInfo teacherInfo) {
        teacherInfo.setId(IdWorker.getId());
        teacherManagementMapper.insert(teacherInfo);
        return new Result(1,"success",null);
    }
}
