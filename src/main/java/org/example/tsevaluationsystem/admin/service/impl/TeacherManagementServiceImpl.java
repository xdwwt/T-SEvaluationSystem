package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.example.tsevaluationsystem.admin.mapper.TeacherManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.UserMapper;
import org.example.tsevaluationsystem.admin.service.TeacherManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherManagementServiceImpl implements TeacherManagementService {

    @Autowired
    private TeacherManagementMapper teacherManagementMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Result insert(TeacherInfo teacherInfo) {
        // 1. 校验工号是否已存在
        QueryWrapper<TeacherInfo> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.eq("teacher_no", teacherInfo.getTeacherNo());
        if (teacherManagementMapper.selectCount(teacherWrapper) > 0) {
            return new Result(0, "工号已存在", null);
        }

        // 2. 校验登录账号是否已存在
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", teacherInfo.getTeacherNo());
        if (userMapper.selectCount(userWrapper) > 0) {
            return new Result(0, "登录账号已存在", null);
        }

        // 3. 插入教师信息
        teacherInfo.setId(IdWorker.getId());
        teacherManagementMapper.insert(teacherInfo);

        // 4. 插入用户登录信息
        UserInfo userInfo = new UserInfo();
        userInfo.setId(IdWorker.getId());
        userInfo.setUserId(teacherInfo.getTeacherNo());  // 用工号作为登录账号
        userInfo.setUsername(teacherInfo.getName());
        userInfo.setPassword("123456");
        userInfo.setStatus(1);  // 教师
        userInfo.setInfoId(teacherInfo.getId());
        userInfo.setIsDele(0);
        userMapper.insert(userInfo);

        return new Result(1, "success", userInfo.getUserId());
    }

    @Override
    public Result list(TeacherInfo teacherInfo) {
        QueryWrapper<TeacherInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        if (teacherInfo != null) {
            if (teacherInfo.getTeacherNo() != null && !teacherInfo.getTeacherNo().isEmpty()) {
                wrapper.like("teacher_no", teacherInfo.getTeacherNo());
            }
            if (teacherInfo.getName() != null && !teacherInfo.getName().isEmpty()) {
                wrapper.like("name", teacherInfo.getName());
            }
            if (teacherInfo.getTitle() != null && !teacherInfo.getTitle().isEmpty()) {
                wrapper.eq("title", teacherInfo.getTitle());
            }
            if (teacherInfo.getDepartment() != null && !teacherInfo.getDepartment().isEmpty()) {
                wrapper.eq("department", teacherInfo.getDepartment());
            }
        }
        wrapper.orderByDesc("create_time");
        return new Result(1, "success", teacherManagementMapper.selectList(wrapper));
    }

    @Override
    public Result resetPassword(String userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserInfo userInfo = userMapper.selectOne(wrapper);
        if (userInfo == null) {
            return new Result(0, "用户不存在", null);
        }
        userInfo.setPassword("123456");
        userMapper.updateById(userInfo);
        return new Result(1, "success", null);
    }
}
