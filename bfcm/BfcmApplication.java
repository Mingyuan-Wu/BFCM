package com.seanco.bfcm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seanco.bfcm.mapper")
public class BfcmApplication {
    public static void main(String[] args) {
        SpringApplication.run(BfcmApplication.class, args);
    }
}
