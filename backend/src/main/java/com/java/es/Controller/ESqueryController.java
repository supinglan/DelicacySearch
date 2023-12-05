package com.java.es.Controller;


import com.java.es.Service.ESqueryService;
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
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ESqueryController {

    @Autowired
    private ESqueryService squeryService;

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
    public ArrayList<Script> search_all(String SearchText)
    {
       return squeryService.executeSearchQuery(SearchText);
    }

    @RequestMapping(value = "/autofill", method = RequestMethod.POST)
    public ArrayList<String> AutoFill(String SearchText)
    {
        ArrayList<String> result = new ArrayList<>();
        if(!Pattern.matches(".*[a-zA-Z].*", SearchText))
        {
            result.addAll(squeryService.AutoFill(SearchText));
            if (result.size() > 9) {
                result = new ArrayList<>(result.subList(0, 9));
            }
        }
        else
        {
            result.addAll(squeryService.pinyin_all(SearchText));
            if (result.size() > 9) {
                result = new ArrayList<>(result.subList(0, 9));
            }
        }
        return result;
    }
}
