package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tb_teacher_evaluation")
public class TeacherEvaluation {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long teacherId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long studentId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long courseId;
    private String semester;

    /**
     * 教学态度评分 1-25
     */
    private Integer teachingScore;

    /**
     * 教学内容评分 1-25
     */
    private Integer contentScore;

    /**
     * 教学方法评分 1-25
     */
    private Integer methodScore;

    /**
     * 教学效果评分 1-25
     */
    private Integer effectScore;

    /**
     * 综合评分 1-100
     */
    private Integer totalScore;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;
}
