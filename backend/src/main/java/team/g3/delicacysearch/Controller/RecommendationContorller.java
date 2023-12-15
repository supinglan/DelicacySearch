package team.g3.delicacysearch.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.g3.delicacysearch.Service.RecommendService;

import java.io.IOException;
import java.util.List;
@RestController
@CrossOrigin
public class RecommendationContorller {
    @Autowired
    private RecommendService recommendService;
    //相关搜索
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public List<String> recommend(String username) throws IOException {
        if(username!=null){
            return recommendService.getRecommend(username);}
        else{
            return recommendService.getRecommend("default");
        }
    }

    //热搜
    @RequestMapping(value = "/hot", method = RequestMethod.POST)
    public List<String> hot() throws IOException {
        return recommendService.getHot();
    }

}
