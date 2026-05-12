package org.example.tsevaluationsystem.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.tsevaluationsystem.dto.Score;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeacherMapper extends BaseMapper<Score> {

    /**
     * 查询教师收到的评价列表
     */
    @Select("SELECT " +
            "  CAST(te.id AS CHAR) as id, CAST(te.student_id AS CHAR) as studentId, CAST(te.course_id AS CHAR) as courseId, te.semester, " +
            "  te.teaching_score as teachingScore, te.content_score as contentScore, " +
            "  te.method_score as methodScore, te.effect_score as effectScore, te.total_score as totalScore, " +
            "  DATE_FORMAT(te.create_time, '%Y-%m-%d %H:%i:%s') as createTime, " +
            "  s.name as studentName, c.course_name as courseName " +
            "FROM tb_teacher_evaluation te " +
            "JOIN tb_student_info s ON te.student_id = s.id " +
            "JOIN tb_course c ON te.course_id = c.id " +
            "WHERE te.teacher_id = #{teacherId} " +
            "  AND te.is_dele = 0 " +
            "  AND s.is_dele = 0 " +
            "ORDER BY te.create_time DESC")
    List<Map<String, Object>> selectEvaluationList(@Param("teacherId") Long teacherId);

    /**
     * 查询教师授课的班级课程列表
     */
    @Select("SELECT " +
            "  ct.id, CAST(ct.class_id AS CHAR) as classId, CAST(ct.course_id AS CHAR) as courseId, ct.semester, " +
            "  ci.class_name as className, ci.grade, c.course_name as courseName " +
            "FROM tb_class_teacher ct " +
            "JOIN tb_class_info ci ON ct.class_id = ci.id " +
            "JOIN tb_course c ON ct.course_id = c.id " +
            "WHERE ct.teacher_id = #{teacherId} " +
            "  AND ct.is_dele = 0 " +
            "  AND ci.is_dele = 0 " +
            "  AND c.is_dele = 0 " +
            "ORDER BY ct.semester DESC, ci.class_name")
    List<Map<String, Object>> selectTeachingClasses(@Param("teacherId") Long teacherId);

    /**
     * 查询某班级课程的学生列表（带已有成绩）
     */
    @Select("SELECT " +
            "  CAST(s.id AS CHAR) as studentId, s.student_no as studentNo, s.name as studentName, " +
            "  CAST(sc.id AS CHAR) as scoreId, sc.usual_score as usualScore, sc.final_score as finalScore, " +
            "  sc.score, sc.comment, sc.is_viewable as isViewable " +
            "FROM tb_class_student cs " +
            "JOIN tb_student_info s ON cs.student_id = s.id " +
            "LEFT JOIN tb_score sc ON sc.student_id = s.id " +
            "  AND sc.course_id = #{courseId} " +
            "  AND sc.teacher_id = #{teacherId} " +
            "  AND sc.semester = #{semester} " +
            "  AND sc.is_dele = 0 " +
            "WHERE cs.class_id = #{classId} " +
            "  AND cs.is_dele = 0 " +
            "  AND s.is_dele = 0 " +
            "ORDER BY s.student_no")
    List<Map<String, Object>> selectClassStudentsWithScore(
            @Param("teacherId") Long teacherId,
            @Param("classId") Long classId,
            @Param("courseId") Long courseId,
            @Param("semester") String semester);

    /**
     * 查询教师已录入的成绩列表
     */
    @Select("SELECT " +
            "  CAST(sc.id AS CHAR) as id, CAST(sc.student_id AS CHAR) as studentId, CAST(sc.course_id AS CHAR) as courseId, CAST(sc.class_id AS CHAR) as classId, " +
            "  sc.semester, sc.usual_score as usualScore, sc.final_score as finalScore, " +
            "  sc.score, sc.comment, sc.is_viewable as isViewable, DATE_FORMAT(sc.create_time, '%Y-%m-%d %H:%i:%s') as createTime, " +
            "  s.name as studentName, s.student_no as studentNo, " +
            "  c.course_name as courseName, ci.class_name as className " +
            "FROM tb_score sc " +
            "JOIN tb_student_info s ON sc.student_id = s.id " +
            "JOIN tb_course c ON sc.course_id = c.id " +
            "JOIN tb_class_info ci ON sc.class_id = ci.id " +
            "WHERE sc.teacher_id = #{teacherId} " +
            "  AND sc.is_dele = 0 " +
            "ORDER BY sc.create_time DESC")
    List<Map<String, Object>> selectScoreList(@Param("teacherId") Long teacherId);
}
