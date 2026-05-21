package org.example.tsevaluationsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 教学评价系统主启动类
 */
@SpringBootApplication
@MapperScan("org.example.tsevaluationsystem.**.mapper")
public class TSEvaluationSystemApplication {

    /**
     * 系统入口主方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TSEvaluationSystemApplication.class, args);
    }

}
