package org.example.tsevaluationsystem.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Score;
import org.example.tsevaluationsystem.dto.TeacherEvaluation;
import org.example.tsevaluationsystem.student.mapper.StudentMapper;
import org.example.tsevaluationsystem.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 学生业务实现类
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 查询学生待评价的教师列表
     * @param studentId 学生ID
     * @return 待评价教师列表
     */
    @Override
    public Result getTeachersToEvaluate(Long studentId) {
        List<Map<String, Object>> list = studentMapper.selectTeachersToEvaluate(studentId);
        return new Result(1, "success", list);
    }

    /**
     * 提交教师评价
     * <p>校验成绩已录入且未重复评价后，保存评价记录并将成绩设为可查看</p>
     * @param studentId 学生ID
     * @param params 评价参数（包含teacherId、courseId、各项评分等）
     * @return 提交结果
     */
    @Override
    public Result submitEvaluation(Long studentId, Map<String, Object> params) {
        Long teacherId = Long.valueOf(params.get("teacherId").toString());
        Long courseId = Long.valueOf(params.get("courseId").toString());
        String semester = (String) params.get("semester");

        // 校验成绩是否已录入：只有成绩录入后才能评价
        QueryWrapper<Score> scoreWrapper = new QueryWrapper<>();
        scoreWrapper.eq("student_id", studentId)
                .eq("teacher_id", teacherId)
                .eq("course_id", courseId)
                .eq("semester", semester)
                .eq("is_dele", 0);
        long scoreCount = studentMapper.selectScoreCount(scoreWrapper);
        if (scoreCount == 0) {
            return new Result(0, "教师尚未录入该课程成绩，暂不能评价", null);
        }

        // 防重复提交：检查是否已评价过
        QueryWrapper<TeacherEvaluation> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId)
                .eq("teacher_id", teacherId)
                .eq("course_id", courseId)
                .eq("semester", semester)
                .eq("is_dele", 0);
        long existingCount = studentMapper.selectCount(wrapper);
        if (existingCount > 0) {
            return new Result(0, "您已对该教师提交过评价，请勿重复评价", null);
        }

        Integer teachingScore = Integer.valueOf(params.get("teachingScore").toString());
        Integer contentScore = Integer.valueOf(params.get("contentScore").toString());
        Integer methodScore = Integer.valueOf(params.get("methodScore").toString());
        Integer effectScore = Integer.valueOf(params.get("effectScore").toString());

        // 校验分数范围
        if (teachingScore < 1 || teachingScore > 25 || contentScore < 1 || contentScore > 25
                || methodScore < 1 || methodScore > 25 || effectScore < 1 || effectScore > 25) {
            return new Result(0, "各项评分必须在1-25分之间", null);
        }

        int totalScore = teachingScore + contentScore + methodScore + effectScore;

        TeacherEvaluation evaluation = new TeacherEvaluation();
        evaluation.setTeacherId(teacherId);
        evaluation.setStudentId(studentId);
        evaluation.setCourseId(courseId);
        evaluation.setSemester(semester);
        evaluation.setTeachingScore(teachingScore);
        evaluation.setContentScore(contentScore);
        evaluation.setMethodScore(methodScore);
        evaluation.setEffectScore(effectScore);
        evaluation.setTotalScore(totalScore);

        int rows = studentMapper.insert(evaluation);
        if (rows > 0) {
            // 评价成功后，将该课程成绩设为可查看
            studentMapper.updateScoreViewable(studentId, teacherId, courseId, semester);
            return new Result(1, "评价提交成功", null);
        }
        return new Result(0, "评价提交失败", null);
    }

    /**
     * 查询学生已提交的评价记录
     * @param studentId 学生ID
     * @return 已评价记录列表
     */
    @Override
    public Result getEvaluationList(Long studentId) {
        List<Map<String, Object>> list = studentMapper.selectEvaluationList(studentId);
        return new Result(1, "success", list);
    }

    /**
     * 查询学生成绩列表（仅返回已可查看的成绩）
     * @param studentId 学生ID
     * @return 成绩列表
     */
    @Override
    public Result getScoreList(Long studentId) {
        List<Map<String, Object>> list = studentMapper.selectScoreList(studentId);
        return new Result(1, "success", list);
    }
}
