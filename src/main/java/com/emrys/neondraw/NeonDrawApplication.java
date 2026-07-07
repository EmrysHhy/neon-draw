package com.emrys.neondraw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.emrys.neondraw.dao.mapper")
public class NeonDrawApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeonDrawApplication.class, args);

    }

}
