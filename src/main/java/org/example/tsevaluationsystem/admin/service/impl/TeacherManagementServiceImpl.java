package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.DepartmentManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.TeacherManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.UserMapper;
import org.example.tsevaluationsystem.admin.service.TeacherManagementService;
import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 教师管理服务实现类
 */
@Service
public class TeacherManagementServiceImpl implements TeacherManagementService {

    @Autowired
    private TeacherManagementMapper teacherManagementMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartmentManagementMapper departmentManagementMapper;

    /**
     * 新增教师（同时创建登录账号）
     * @param teacherInfo 教师信息
     * @return 操作结果
     */
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

    /**
     * 查询教师列表（分页）
     * @param params 查询参数
     * @return 教师列表
     */
    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<TeacherInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("teacherNo") != null && !params.get("teacherNo").toString().isEmpty()) {
            wrapper.like("teacher_no", params.get("teacherNo").toString());
        }
        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            wrapper.like("name", params.get("name").toString());
        }
        if (params.get("titleId") != null && !params.get("titleId").toString().isEmpty()) {
            wrapper.eq("title_id", params.get("titleId").toString());
        }
        if (params.get("deptName") != null && !params.get("deptName").toString().isEmpty()) {
            QueryWrapper<Department> deptWrapper = new QueryWrapper<>();
            deptWrapper.like("dept_name", params.get("deptName").toString());
            deptWrapper.eq("is_dele", 0);
            List<Department> deptList = departmentManagementMapper.selectList(deptWrapper);
            List<Long> deptIds = deptList.stream().map(Department::getId).collect(Collectors.toList());
            if (!deptIds.isEmpty()) {
                wrapper.in("department_id", deptIds);
            } else {
                wrapper.eq("department_id", -1L);
            }
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<TeacherInfo> list = teacherManagementMapper.selectList(wrapper);
        PageInfo<TeacherInfo> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    /**
     * 查询所有教师
     * @return 教师列表
     */
    @Override
    public Result listAll() {
        QueryWrapper<TeacherInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        wrapper.orderByAsc("name");
        List<TeacherInfo> list = teacherManagementMapper.selectList(wrapper);
        return new Result(1, "success", list);
    }

    /**
     * 重置教师密码
     * @param userId 教师登录账号
     * @return 操作结果
     */
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

    /**
     * 删除教师（逻辑删除，同时删除登录账号）
     * @param userId 教师登录账号
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result delete(String userId) {
        // 1. 逻辑删除用户登录信息
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", userId);
        UserInfo userInfo = userMapper.selectOne(userWrapper);
        if (userInfo == null) {
            return new Result(0, "用户不存在", null);
        }
        userInfo.setIsDele(1);
        userMapper.updateById(userInfo);

        // 2. 逻辑删除教师信息
        QueryWrapper<TeacherInfo> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.eq("teacher_no", userId);
        TeacherInfo teacherInfo = teacherManagementMapper.selectOne(teacherWrapper);
        if (teacherInfo == null) {
            return new Result(0, "教师不存在", null);
        }
        teacherInfo.setIsDele(1);
        teacherManagementMapper.updateById(teacherInfo);

        return new Result(1, "success", null);
    }

    /**
     * 编辑教师（同步更新登录账号姓名）
     * @param teacherInfo 教师信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result update(TeacherInfo teacherInfo) {
        // 1. 校验教师是否存在
        TeacherInfo exist = teacherManagementMapper.selectById(teacherInfo.getId());
        if (exist == null) {
            return new Result(0, "教师不存在", null);
        }

        // 2. 更新教师信息（工号不允许修改）
        teacherManagementMapper.updateById(teacherInfo);

        // 3. 同步更新用户登录信息中的姓名
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", exist.getTeacherNo());
        UserInfo userInfo = userMapper.selectOne(userWrapper);
        if (userInfo != null) {
            userInfo.setUsername(teacherInfo.getName());
            userMapper.updateById(userInfo);
        }

        return new Result(1, "success", null);
    }
}
