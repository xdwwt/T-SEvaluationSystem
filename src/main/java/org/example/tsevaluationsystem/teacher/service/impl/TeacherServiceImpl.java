package org.example.tsevaluationsystem.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Score;
import org.example.tsevaluationsystem.teacher.mapper.TeacherMapper;
import org.example.tsevaluationsystem.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Result getEvaluationList(Long teacherId) {
        List<Map<String, Object>> list = teacherMapper.selectEvaluationList(teacherId);
        return new Result(1, "success", list);
    }

    @Override
    public Result getTeachingClasses(Long teacherId) {
        List<Map<String, Object>> list = teacherMapper.selectTeachingClasses(teacherId);
        return new Result(1, "success", list);
    }

    @Override
    public Result getClassStudentsWithScore(Long teacherId, Long classId, Long courseId, String semester) {
        List<Map<String, Object>> list = teacherMapper.selectClassStudentsWithScore(teacherId, classId, courseId, semester);
        return new Result(1, "success", list);
    }

    @Override
    @Transactional
    public Result submitScores(Long teacherId, List<Map<String, Object>> scoreList) {
        if (scoreList == null || scoreList.isEmpty()) {
            return new Result(0, "成绩列表为空", null);
        }

        int successCount = 0;
        for (Map<String, Object> item : scoreList) {
            Long studentId = Long.valueOf(item.get("studentId").toString());
            Long classId = Long.valueOf(item.get("classId").toString());
            Long courseId = Long.valueOf(item.get("courseId").toString());
            String semester = (String) item.get("semester");

            Object usualScoreObj = item.get("usualScore");
            Object finalScoreObj = item.get("finalScore");
            Object scoreObj = item.get("score");

            BigDecimal usualScore = (usualScoreObj != null && !usualScoreObj.toString().trim().isEmpty())
                    ? new BigDecimal(usualScoreObj.toString()) : null;
            BigDecimal finalScore = (finalScoreObj != null && !finalScoreObj.toString().trim().isEmpty())
                    ? new BigDecimal(finalScoreObj.toString()) : null;
            BigDecimal score = (scoreObj != null && !scoreObj.toString().trim().isEmpty())
                    ? new BigDecimal(scoreObj.toString()) : null;
            String comment = item.get("comment") != null ? (String) item.get("comment") : null;
            Integer isViewable = item.get("isViewable") != null ? Integer.valueOf(item.get("isViewable").toString()) : 0;

            // 查询是否已有成绩记录
            QueryWrapper<Score> wrapper = new QueryWrapper<>();
            wrapper.eq("student_id", studentId)
                    .eq("course_id", courseId)
                    .eq("teacher_id", teacherId)
                    .eq("semester", semester)
                    .eq("is_dele", 0);
            Score existing = teacherMapper.selectOne(wrapper);

            if (existing != null) {
                // 更新
                existing.setUsualScore(usualScore);
                existing.setFinalScore(finalScore);
                existing.setScore(score);
                existing.setComment(comment);
                existing.setIsViewable(isViewable);
                teacherMapper.updateById(existing);
            } else {
                // 新增
                Score newScore = new Score();
                newScore.setStudentId(studentId);
                newScore.setCourseId(courseId);
                newScore.setTeacherId(teacherId);
                newScore.setClassId(classId);
                newScore.setSemester(semester);
                newScore.setUsualScore(usualScore);
                newScore.setFinalScore(finalScore);
                newScore.setScore(score);
                newScore.setComment(comment);
                newScore.setIsViewable(isViewable);
                teacherMapper.insert(newScore);
            }
            successCount++;
        }

        return new Result(1, "成功录入 " + successCount + " 条成绩", null);
    }

    @Override
    public Result getScoreList(Long teacherId) {
        List<Map<String, Object>> list = teacherMapper.selectScoreList(teacherId);
        return new Result(1, "success", list);
    }
}
