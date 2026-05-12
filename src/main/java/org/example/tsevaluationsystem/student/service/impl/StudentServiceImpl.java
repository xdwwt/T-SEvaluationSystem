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

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result getTeachersToEvaluate(Long studentId) {
        List<Map<String, Object>> list = studentMapper.selectTeachersToEvaluate(studentId);
        return new Result(1, "success", list);
    }

    @Override
    public Result submitEvaluation(Long studentId, Map<String, Object> params) {
        Long teacherId = Long.valueOf(params.get("teacherId").toString());
        Long courseId = Long.valueOf(params.get("courseId").toString());
        String semester = (String) params.get("semester");

        // 校验成绩是否已发放：只有成绩发放后才能评价
        QueryWrapper<Score> scoreWrapper = new QueryWrapper<>();
        scoreWrapper.eq("student_id", studentId)
                .eq("teacher_id", teacherId)
                .eq("course_id", courseId)
                .eq("semester", semester)
                .eq("is_viewable", 1)
                .eq("is_dele", 0);
        long scoreCount = studentMapper.selectScoreCount(scoreWrapper);
        if (scoreCount == 0) {
            return new Result(0, "教师尚未发放该课程成绩，暂不能评价", null);
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
            return new Result(1, "评价提交成功", null);
        }
        return new Result(0, "评价提交失败", null);
    }

    @Override
    public Result getEvaluationList(Long studentId) {
        List<Map<String, Object>> list = studentMapper.selectEvaluationList(studentId);
        return new Result(1, "success", list);
    }

    @Override
    public Result getScoreList(Long studentId) {
        List<Map<String, Object>> list = studentMapper.selectScoreList(studentId);
        return new Result(1, "success", list);
    }
}
