package com.java.es.Service;

import com.java.es.pojo.Script;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class ESqueryService {
    @Autowired
    public RestHighLevelClient elasticsearchClient;

    public ArrayList<Script> executeSearchQuery(String SearchText)
    {
        boolean containsLetter = Pattern.matches(".*[a-zA-Z].*", SearchText);
        if(containsLetter)
        {
            List<String> results = getNames(SearchText);
            ArrayList<Script> total_results = new ArrayList<>();
            for (String result : results) {
                ArrayList<Script> part_results = executeSearchQuery(result);
                for (Script partResult : part_results) {
                    if (!total_results.contains(partResult)) {
                        total_results.add(partResult);
                    }
                }
            }
            if (total_results.size() > 9) {
                total_results = new ArrayList<>(total_results.subList(0, 9));
            }
            return total_results;
        }
        else
        {
            ArrayList<Script> result = new ArrayList<>();
            try {
                SearchRequest searchRequest = new SearchRequest("script_index");
                MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                        .field("title", 3.14F)  // 设置"title"字段的权重为3.0
                        .field("abstract", 2.0F)  // 设置"abstract"字段的权重为2.0
                        .field("ingredient", 1.5F)  // 设置"ingredient"字段的权重为1.5
                        .field("steps", 1.0F)  // 设置"steps"字段的权重为1.0
                        .field("origin", 0.5F);
                searchRequest.source().query(queryBuilder);
                searchRequest.source().fetchSource(true);
                searchRequest.source().sort("_score", SortOrder.DESC);
                searchRequest.source().size(200);
                SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                long hitnum = hits.getTotalHits().value;
                if(hitnum == 0) System.out.println("无结果！");
                for (SearchHit hit : hits) {
                    // 处理每个命中的文档
                    String url = hit.getSourceAsMap().get("html_url").toString();
                    String pic_url = hit.getSourceAsMap().get("pict_url").toString();
                    String title = hit.getSourceAsMap().get("title").toString();
                    String Abstract = hit.getSourceAsMap().get("abstract").toString();
                    String source = hit.getSourceAsMap().get("origin").toString();
                    ArrayList<String> steps = new ArrayList<>();
                    List<Object> stepsList = (List<Object>) hit.getSourceAsMap().get("steps");
                    if (stepsList != null) {
                        for (Object step : stepsList) {
                            if (step instanceof String) {
                                steps.add((String) step);
                            }
                        }
                    }
                    ArrayList<String> ingredients = new ArrayList<>();
                    List<Object> ingreList = (List<Object>) hit.getSourceAsMap().get("steps");
                    if (stepsList != null) {
                        for (Object step : ingreList) {
                            if (step instanceof String) {
                                ingredients.add((String) step);
                            }
                        }
                    }
                    float score = hit.getScore();
                    // 在这里执行您希望的操作，比如打印或处理结果
                    System.out.println("菜名: " + title);
                    System.out.println("摘要:" + Abstract);
                    System.out.println(" ");
//                System.out.println("网页地址: " + url);
//                System.out.println("图片地址: " + pic_url);
                    Script ele = new Script(pic_url, url, title, Abstract, ingredients, steps, source);
                    result.add(ele);
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    public ArrayList<String> AutoFill(String SearchText)
    {
        ArrayList<String> result = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest("script_index");
            MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                    .field("title", 3.14F)  // 设置"title"字段的权重为3.0
                    .field("abstract", 2.0F)  // 设置"abstract"字段的权重为2.0
                    .field("ingredient", 1.5F)  // 设置"ingredient"字段的权重为1.5
                    .field("steps", 1.0F)  // 设置"steps"字段的权重为1.0
                    .field("origin", 0.5F);
            searchRequest.source().query(queryBuilder);
            searchRequest.source().fetchSource(true);
            searchRequest.source().sort("_score", SortOrder.DESC);
            searchRequest.source().size(200);
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            long hitnum = hits.getTotalHits().value;
            if(hitnum == 0) System.out.println("无结果！");
            for (SearchHit hit : hits) {
                // 处理每个命中的文档

                String title = hit.getSourceAsMap().get("title").toString();

                if (!result.contains(title)) {
                    result.add(title);
                }
            }
            if (result.size() > 9) {
                result = new ArrayList<>(result.subList(0, 9));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public ArrayList<String> getNames(String SearchText) {
        ArrayList<String> result = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest("pigg_test_pinyin");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery("name.pinyin", SearchText));
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                String name = hit.getSourceAsMap().get("name").toString();
                if(!result.contains(name)) result.add(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
