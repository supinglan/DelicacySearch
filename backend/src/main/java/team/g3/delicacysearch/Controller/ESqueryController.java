package team.g3.delicacysearch.Controller;



import org.springframework.web.bind.annotation.*;
import team.g3.delicacysearch.Service.ESqueryService;
import team.g3.delicacysearch.Service.RecommendService;
import team.g3.delicacysearch.pojo.Pair;
import team.g3.delicacysearch.pojo.Script;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ESqueryController {

    @Autowired
    private ESqueryService squeryService;
    @Autowired
    private RecommendService recommendService;


    //searchtext:搜索内容
    //type：按照何种方式搜索（食材、steps）
    //sortType：按照何种方式排序（按clicks、score、综合排序）
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Pair<Integer, ArrayList<Script>> selectByTag(String SearchText, Integer Method, Integer Taste, Integer Scene, Integer Category, Integer type, Integer sortType, String username, Integer currentPage) throws IOException {
        if(username!=null){
            recommendService.updateSearchHistory(username, SearchText);}
        else{
            recommendService.updateSearchHistory("default", SearchText);
        }
        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(Method);
        ints.add(Taste);
        ints.add(Scene);
        ints.add(Category);
        ArrayList<Script> results = squeryService.search(SearchText, ints, type, sortType);
        ArrayList<Script> subresults = new ArrayList<>(results.subList(8*(currentPage-1), Math.min(8*currentPage, results.size())));
        return new Pair<Integer, ArrayList<Script>>(results.size(), subresults) {
        };
    }

    @RequestMapping(value = "/autofill", method = RequestMethod.POST)
    public ArrayList<String> AutoFill(String SearchText)
    {
        ArrayList<String> result = new ArrayList<>();
        if(!Pattern.matches(".*[a-zA-Z].*", SearchText))
        {
            result.addAll(squeryService.AutoFill(SearchText));
        }
        else
        {
            result.addAll(squeryService.pinyin_all(SearchText));
        }
        if (result.size() > 9) {
            result = new ArrayList<>(result.subList(0, 9));
        }
        return result;
    }

}
