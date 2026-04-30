package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.tsevaluationsystem.dto.ClassStudent;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClassStudentMapper extends BaseMapper<ClassStudent> {

    @Select("SELECT cs.id, s.id as studentId, s.student_no as studentNo, s.name, s.gender, s.grade, s.major " +
            "FROM tb_class_student cs " +
            "JOIN tb_student_info s ON cs.student_id = s.id " +
            "WHERE cs.class_id = #{classId} AND cs.is_dele = 0 AND s.is_dele = 0 " +
            "ORDER BY s.student_no")
    List<Map<String, Object>> selectStudentsByClassId(@Param("classId") Long classId);

    @Select("SELECT id, student_no as studentNo, name, gender, grade, major " +
            "FROM tb_student_info " +
            "WHERE is_dele = 0 AND id NOT IN (" +
            "  SELECT student_id FROM tb_class_student WHERE is_dele = 0" +
            ") " +
            "ORDER BY student_no")
    List<Map<String, Object>> selectUnassignedStudents();
}
