
package team.g3.delicacysearch.Service;

import team.g3.delicacysearch.dao.FoodTripleMapper;
import team.g3.delicacysearch.model.FoodTriple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.g3.delicacysearch.model.KnowledgeGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeGraphService {
    @Autowired
    private ESqueryService squeryService;
    @Autowired
    private FoodTripleMapper foodTripleMapper;
    public KnowledgeGraph getKnowledgeGraph(String searchText) throws IOException {
        String cuisine = squeryService.executeSearchQuery(searchText,0).get(0).getTitle();
        List<String> cuisines = new ArrayList<>();
        cuisines.add(cuisine);
        List<String> ingredients = new ArrayList<>();
        List<FoodTriple> first = foodTripleMapper.selectBySource(cuisine);
        //将first的所有内容加入result中
        List<FoodTriple> result = new ArrayList<>(first);
        for (FoodTriple foodTriple : first) {
            List<FoodTriple> second = foodTripleMapper.selectByTarget(foodTriple.getTarget());
            ingredients.add(foodTriple.getTarget());
            //将second中不在result中的内容加入result中
            for (FoodTriple foodTriple1 : second) {
                if (!result.contains(foodTriple1)) {
                    result.add(foodTriple1);
                    cuisines.add(foodTriple1.getSource());
                }
            }
        }
        return new KnowledgeGraph(cuisines, ingredients, result);
    }
}
