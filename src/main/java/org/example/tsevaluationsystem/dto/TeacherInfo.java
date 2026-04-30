package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("tb_teacher_info")
public class TeacherInfo {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String teacherNo;
    private String name;

    /**
     * 性别
     * 0：女
     * 1：男
     */
    private Integer gender;

    private String title;
    private String department;
    private String phone;
    private String email;
    private LocalDate entryDate;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
