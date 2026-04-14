package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_teacher_info")
public class TeacherInfo {
    private Long id;
    private String name;
}
