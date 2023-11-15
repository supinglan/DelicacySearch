package pers.spl.ziot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@MapperScan("pers.spl.ziot.dao")
public class ZiotApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZiotApplication.class, args);
    }
}
