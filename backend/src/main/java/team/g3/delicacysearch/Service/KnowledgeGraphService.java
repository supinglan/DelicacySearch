
package team.g3.delicacysearch.Service;

import team.g3.delicacysearch.dao.FoodTripleMapper;
import team.g3.delicacysearch.model.FoodTriple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.g3.delicacysearch.model.KnowledgeGraph;
import team.g3.delicacysearch.model.Line;
import team.g3.delicacysearch.model.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeGraphService {
    @Autowired
    private ESqueryService squeryService;
    @Autowired
    private FoodTripleMapper foodTripleMapper;

    static int stop_num=20;
    public KnowledgeGraph getKnowledgeGraph(String searchText) throws IOException {
        List<Node> nodes = new ArrayList<>();
        List<Line> lines = new ArrayList<>();
        List<FoodTriple> first = foodTripleMapper.selectBySource(searchText);
        nodes.add(new Node(searchText, searchText,"#38419D"));
        for (FoodTriple foodTriple : first) {
            System.out.println(foodTriple.getTarget());
            List<FoodTriple> second = foodTripleMapper.selectByTarget(foodTriple.getTarget());
            if(second.size()>stop_num) continue;
            lines.add(new Line(searchText,foodTriple.getTarget()));
            nodes.add(new Node(foodTriple.getTarget(),foodTriple.getTarget(),"#52D3D8"));
            int i=0;
            for (FoodTriple foodTriple1 : second) {
                if(foodTriple1.getSource().equals(searchText)) continue;
                nodes.add(new Node(foodTriple1.getSource(), foodTriple1.getSource(), "#3887BE"));
                lines.add(new Line(foodTriple1.getSource(), foodTriple1.getTarget()));
                i++;
                if(i>3) break;
            }
        }
        System.out.println(nodes.size());
        return new KnowledgeGraph(searchText,nodes,lines);
    }
}
