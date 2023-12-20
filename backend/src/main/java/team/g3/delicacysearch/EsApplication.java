package team.g3.delicacysearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@MapperScan("team.g3.delicacysearch.dao")
public class EsApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EsApplication.class, args);
    }

}