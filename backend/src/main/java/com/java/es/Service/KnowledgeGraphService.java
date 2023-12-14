package com.java.es.Service;

import com.java.es.dao.FoodTripleMapper;
import com.java.es.model.FoodTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeGraphService {
    @Autowired
    private ESqueryService squeryService;
    @Autowired
    private FoodTripleMapper foodTripleMapper;
    public List<FoodTriple> getKnowledgeGraph(String searchText) throws IOException {
        String cuisine = squeryService.executeSearchQuery(searchText).get(0).getTitle();
        List<FoodTriple> first = foodTripleMapper.selectBySource(cuisine);
        //将first的所有内容加入result中
        List<FoodTriple> result = new ArrayList<>(first);
        for (FoodTriple foodTriple : first) {
            List<FoodTriple> second = foodTripleMapper.selectBySource(foodTriple.getTarget());
            result.addAll(second);
        }
        return result;
    }
}
