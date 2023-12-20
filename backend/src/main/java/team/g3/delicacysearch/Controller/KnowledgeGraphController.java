
package team.g3.delicacysearch.Controller;

import team.g3.delicacysearch.Service.KnowledgeGraphService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.g3.delicacysearch.model.KnowledgeGraph;

import java.io.IOException;


@RestController
@CrossOrigin
public class KnowledgeGraphController {
    @Autowired
    private KnowledgeGraphService knowledgeGraphService;
    @RequestMapping(value = "/getKG", method = RequestMethod.POST)
    public KnowledgeGraph getKnowledgeGraph(String rootText) throws IOException {
        System.out.println(rootText);
        return knowledgeGraphService.getKnowledgeGraph(rootText);
    }
}
