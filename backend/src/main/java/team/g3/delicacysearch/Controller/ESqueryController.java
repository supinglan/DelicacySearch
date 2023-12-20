package team.g3.delicacysearch.Controller;


import team.g3.delicacysearch.Service.ESqueryService;
import team.g3.delicacysearch.Service.RecommendService;
import team.g3.delicacysearch.pojo.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
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
    public ArrayList<Script> selectByTag(String SearchText, Integer Method, Integer Taste, Integer Scene, Integer Category, Integer type, Integer sortType,String username) throws IOException {
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
        return squeryService.search(SearchText, ints, type, sortType);
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
