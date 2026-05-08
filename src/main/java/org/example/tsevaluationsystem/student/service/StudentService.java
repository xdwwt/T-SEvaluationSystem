package org.example.tsevaluationsystem.student.service;

import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface StudentService {

    /**
     * 查询学生待评价的教师列表
     */
    Result getTeachersToEvaluate(Long studentId);

    /**
     * 提交教师评价
     */
    Result submitEvaluation(Long studentId, Map<String, Object> params);

    /**
     * 查询学生已评价记录
     */
    Result getEvaluationList(Long studentId);

    /**
     * 查询学生成绩列表
     */
    Result getScoreList(Long studentId);
}
