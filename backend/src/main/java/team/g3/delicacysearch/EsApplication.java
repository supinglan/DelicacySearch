package team.g3.delicacysearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import team.g3.delicacysearch.Utils.IndexBuilder;

@SpringBootApplication
@MapperScan("team.g3.delicacysearch.dao")
public class EsApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EsApplication.class, args);
    }

}