package com.java.es.Controller;

import com.java.es.Service.KnowledgeGraphService;
import com.java.es.model.FoodTriple;
import com.java.es.pojo.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin
public class KnowledgeGraphController {
    @Autowired
    private KnowledgeGraphService knowledgeGraphService;
    @RequestMapping(value = "/getKnowledgeGraph", method = RequestMethod.POST)
    public List<FoodTriple> getKnowledgeGraph(String SearchText) throws IOException {
        System.out.println(SearchText);
        System.out.println("kd");
        return knowledgeGraphService.getKnowledgeGraph(SearchText);
    }
}
