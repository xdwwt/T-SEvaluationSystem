package org.example.tsevaluationsystem.student.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.tsevaluationsystem.config.JwtUtil;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学生端接口
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 查询待评价的教师列表
     */
    @GetMapping("/teacher/list")
    public Result getTeachersToEvaluate(HttpServletRequest request) {
        Long studentId = JwtUtil.getCurrentInfoId(request);
        if (studentId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return studentService.getTeachersToEvaluate(studentId);
    }

    /**
     * 提交教师评价
     */
    @PostMapping("/evaluation/submit")
    public Result submitEvaluation(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long studentId = JwtUtil.getCurrentInfoId(request);
        if (studentId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return studentService.submitEvaluation(studentId, params);
    }

    /**
     * 查询已评价记录
     */
    @GetMapping("/evaluation/list")
    public Result getEvaluationList(HttpServletRequest request) {
        Long studentId = JwtUtil.getCurrentInfoId(request);
        if (studentId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return studentService.getEvaluationList(studentId);
    }

    /**
     * 查询成绩列表
     */
    @GetMapping("/score/list")
    public Result getScoreList(HttpServletRequest request) {
        Long studentId = JwtUtil.getCurrentInfoId(request);
        if (studentId == null) {
            return new Result(0, "未登录或token无效", null);
        }
        return studentService.getScoreList(studentId);
    }
}
