package org.example.tsevaluationsystem.teacher.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.tsevaluationsystem.config.JwtUtil;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 教师端接口
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 查询教师收到的评价列表
     */
    @GetMapping("/evaluation/list")
    public Result getEvaluationList(HttpServletRequest request) {
        Long teacherId = JwtUtil.getCurrentInfoId(request);
        if (teacherId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return teacherService.getEvaluationList(teacherId);
    }

    /**
     * 查询教师授课的班级课程列表
     */
    @GetMapping("/class/list")
    public Result getTeachingClasses(HttpServletRequest request) {
        Long teacherId = JwtUtil.getCurrentInfoId(request);
        if (teacherId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return teacherService.getTeachingClasses(teacherId);
    }

    /**
     * 查询某班级课程的学生列表（带已有成绩）
     */
    @GetMapping("/class/students")
    public Result getClassStudentsWithScore(HttpServletRequest request,
                                            @RequestParam Long classId,
                                            @RequestParam Long courseId,
                                            @RequestParam String semester) {
        Long teacherId = JwtUtil.getCurrentInfoId(request);
        if (teacherId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return teacherService.getClassStudentsWithScore(teacherId, classId, courseId, semester);
    }

    /**
     * 批量提交/更新成绩
     */
    @PostMapping("/score/submit")
    public Result submitScores(HttpServletRequest request, @RequestBody List<Map<String, Object>> scoreList) {
        Long teacherId = JwtUtil.getCurrentInfoId(request);
        if (teacherId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return teacherService.submitScores(teacherId, scoreList);
    }

    /**
     * 查询教师已录入的成绩列表
     */
    @GetMapping("/score/list")
    public Result getScoreList(HttpServletRequest request) {
        Long teacherId = JwtUtil.getCurrentInfoId(request);
        if (teacherId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return teacherService.getScoreList(teacherId);
    }
}
