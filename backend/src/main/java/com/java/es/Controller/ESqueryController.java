package com.java.es.Controller;


import com.java.es.pojo.Script;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class ESqueryController {

    @Autowired
    private RestHighLevelClient elasticsearchClient;

//    @RequestMapping(value = "/elastic1", method = RequestMethod.POST)
//    public void executeSearchQuery_All(String SearchText) {
//        try {
//            SearchRequest searchRequest = new SearchRequest("http_index");
//            MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(SearchText,"title", "abstract", "pDF", "address","author","bibkey","cite_ACL","cite_Informal","editors","language","month","note","publisher","sIG","venue","volume","year");
//            searchRequest.source().query(queryBuilder);
//            searchRequest.source().fetchSource(new String[] { "_id", "title", "_score" }, null);
//            searchRequest.source().sort("_score", SortOrder.DESC);
//            searchRequest.source().size(164);
//            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHits hits = searchResponse.getHits();
//            long hitnum = hits.getTotalHits().value;
//            if(hitnum == 0) System.out.println("无结果！");
//            for (SearchHit hit : hits) {
//                // 处理每个命中的文档
//                String id = hit.getId();
//                String title = hit.getSourceAsMap().get("title").toString();
//                float score = hit.getScore();
//
//                // 在这里执行您希望的操作，比如打印或处理结果
//                System.out.println("文档编号: " + id);
//                System.out.println("网页地址: " + "https://aclanthology.org/2023.acl-short."+id+"/");
//                System.out.println("正文PDF地址: " + "https://aclanthology.org/2023.acl-short."+id+".pdf");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @RequestMapping(value = "/elastic", method = RequestMethod.POST)
    public ArrayList<Script> executeSearchQuery(String SearchText)
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

    @RequestMapping(value = "/autofill", method = RequestMethod.POST)
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
}
