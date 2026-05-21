package org.example.tsevaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;
    private String mes;
    private Object data;
}
