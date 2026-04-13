package org.example.tsevaluationsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.tsevaluationsystem.**.mapper")
public class TSEvaluationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TSEvaluationSystemApplication.class, args);
    }

}
