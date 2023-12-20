package team.g3.delicacysearch.Service;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.g3.delicacysearch.pojo.Script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService
{
    @Autowired
    public RestHighLevelClient elasticsearchClient;
    @Autowired
    private ESqueryService squeryService;
    String indexName = "search_history";

    public void updateSearchHistory(String username, String keyword) throws IOException {
        int topN = 3;
        AnalyzeRequest analyzeRequest = AnalyzeRequest.
                withGlobalAnalyzer("ik_smart", keyword);

        org.elasticsearch.client.indices.AnalyzeResponse response = elasticsearchClient.indices()
                .analyze(analyzeRequest, RequestOptions.DEFAULT);

        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        //for debug
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            System.out.println("Token: " + token.getTerm());
            IndexRequest indexRequest = new IndexRequest(indexName)
                    .source("user_id", username, "keyword", token.getTerm());
            IndexResponse indexResponse = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }
    public List<String>getRecommend(String username) {
        int topN = 3;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("user_id", username));

        // 构建聚合查询
        TermsAggregationBuilder aggregation = AggregationBuilders
                .terms("topN")
                .field("keyword")
                .size(topN);

        // 构建搜索请求
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source()
                .query(boolQuery)
                .aggregation(aggregation)
                .size(0);
        List<String> resultList = new ArrayList<>();
        try {
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

            // 解析聚合结果
            ParsedStringTerms topNTerms = response.getAggregations().get("topN");

            int size = topNTerms.getBuckets().size();
            for (Bucket bucket : topNTerms.getBuckets()) {
                String keyword = bucket.getKeyAsString();
                ArrayList<Script> scripts=  squeryService.executeSearchQuery(keyword, 0);
                int i= 0;
                for (Script script : scripts) {
                    String title = script.getTitle();
                    // 检查是否已经包含该标题，如果不包含则添加到结果列表和集合中
                    if (!resultList.contains(title)) {
                        resultList.add(title);
                        i++;
                        // 如果已经找到了三个不重复的标题，跳出循环
                        if (i >= 8/size+1) {
                            break;
                        }
                    }
                }
                long docCount = bucket.getDocCount();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(resultList.size()<8)
        {
            return resultList;
        }
        return resultList.subList(0, 8);
    }
    public List<String>getHot(){
        int topN = 8;
        TermsAggregationBuilder aggregation = AggregationBuilders
                .terms("topN")
                .field("keyword")
                .size(topN);

        // 构建搜索请求
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source()
                .aggregation(aggregation)
                .size(0);
        List<String> resultList = new ArrayList<>();
        try {
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

            // 解析聚合结果
            ParsedStringTerms topNTerms = response.getAggregations().get("topN");

            int size = topNTerms.getBuckets().size();
            for (Bucket bucket : topNTerms.getBuckets()) {
                String keyword = bucket.getKeyAsString();
                ArrayList<Script> scripts=  squeryService.executeSearchQuery(keyword,0);
                int i= 0;
                for (Script script : scripts) {
                    String title = script.getTitle();
                    // 检查是否已经包含该标题，如果不包含则添加到结果列表和集合中
                    if (!resultList.contains(title)) {
                        resultList.add(title);
                        i++;
                        // 如果已经找到了三个不重复的标题，跳出循环
                        if (i >= 8/size+1) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(resultList.size()<8)
        {
            return resultList;
        }
        return resultList.subList(0, 8);
    }
}
