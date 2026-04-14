package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;

public interface TeacherManagementService {
    /**
     * 新增教师
     * @param teacherInfo
     * @return
     */
    Result insert(TeacherInfo teacherInfo);
}
