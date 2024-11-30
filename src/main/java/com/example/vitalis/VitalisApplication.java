package com.example.vitalis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.vitalis.mapper")
public class VitalisApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitalisApplication.class, args);
    }

}
