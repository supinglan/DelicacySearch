package team.g3.delicacysearch.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient()
    {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("120.55.14.3",9200, "http")));
    }

}
