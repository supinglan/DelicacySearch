package team.g3.delicacysearch.Utils;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class IndexInitializer implements CommandLineRunner {
        @Autowired
        private RestHighLevelClient elasticsearchClient;
        @Override
        public void run(String... args) throws Exception {
                String indexName = "search_history";
                GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
                if (!elasticsearchClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
                        CreateIndexRequest request = new CreateIndexRequest(indexName);
                        request.settings(Settings.builder()
                                .put("index.number_of_shards", 3)
                                .put("index.number_of_replicas", 2)
                        );
                        XContentBuilder mapping = XContentFactory.jsonBuilder();
                        mapping.startObject()
                                .startObject("properties")
                                .startObject("user_id")
                                .field("type", "keyword")
                                .endObject()
                                .startObject("keyword")
                                .field("type",  "keyword")
                                .endObject()
                                .endObject()
                                .endObject();
                        request.mapping(mapping);
                        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(request, RequestOptions.DEFAULT);
                        if (createIndexResponse.isAcknowledged()) {
                                System.out.println("history_keywords索引初始化成功！");
                        } else {
                                System.out.println("history_keywords索引初始化失败！");
                        }
                }
        }
}
