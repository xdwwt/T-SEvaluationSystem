package org.example.tsevaluationsystem.teacher.service;

import org.example.tsevaluationsystem.dto.Result;

import java.util.List;
import java.util.Map;

/**
 * 教师业务接口
 */
public interface TeacherService {

    /**
     * 查询教师收到的评价列表
     * @param teacherId 教师ID
     * @return 评价列表
     */
    Result getEvaluationList(Long teacherId);

    /**
     * 查询教师授课的班级课程列表
     * @param teacherId 教师ID
     * @return 班级课程列表
     */
    Result getTeachingClasses(Long teacherId);

    /**
     * 查询某班级课程的学生列表（带已有成绩）
     * @param teacherId 教师ID
     * @param classId 班级ID
     * @param courseId 课程ID
     * @param semester 学期
     * @return 学生列表
     */
    Result getClassStudentsWithScore(Long teacherId, Long classId, Long courseId, String semester);

    /**
     * 批量提交/更新成绩
     * @param teacherId 教师ID
     * @param scoreList 成绩列表
     * @return 操作结果
     */
    Result submitScores(Long teacherId, List<Map<String, Object>> scoreList);

    /**
     * 查询教师已录入的成绩列表
     * @param teacherId 教师ID
     * @return 成绩列表
     */
    Result getScoreList(Long teacherId);
}
