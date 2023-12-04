package com.java.es.Controller;


import com.java.es.Service.ESqueryService;
import com.java.es.pojo.Script;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ESqueryController {
    @Autowired
    private ESqueryService squeryService;

    @RequestMapping(value = "/elastic", method = RequestMethod.POST)
    public ArrayList<Script> getsearchResult(String SearchText)
    {
        return squeryService.executeSearchQuery(SearchText);
    }

    @RequestMapping(value = "/autofill", method = RequestMethod.POST)
    public ArrayList<String> autofills(String SearchText)
    {
        return squeryService.AutoFill(SearchText);
    }


    @RequestMapping(value = "/pinyin", method = RequestMethod.POST)
    public ArrayList<String> getnames(String SearchText)
    {
        return squeryService.getNames(SearchText);
    }

}



    