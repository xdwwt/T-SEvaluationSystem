package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 学生信息实体类
 */
@Data
@TableName("tb_student_info")
public class StudentInfo {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String studentNo;
    private String name;

    /**
     * 性别
     * 0：女
     * 1：男
     */
    private Integer gender;

    private String grade;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long majorId;

    private String phone;
    private String email;

    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long classId;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
