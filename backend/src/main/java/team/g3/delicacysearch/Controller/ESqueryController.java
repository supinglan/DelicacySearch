package team.g3.delicacysearch.Controller;


import team.g3.delicacysearch.Service.ESqueryService;
import team.g3.delicacysearch.Service.IngredientSearchService;
import team.g3.delicacysearch.Service.RecommendService;
import team.g3.delicacysearch.Service.StepsSearchService;
import team.g3.delicacysearch.pojo.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ESqueryController {

    @Autowired
    private ESqueryService squeryService;
    @Autowired
    private IngredientSearchService ingredientSearchService;
    @Autowired
    private StepsSearchService stepsSearchService;
    @Autowired
    private RecommendService recommendService;
    @RequestMapping(value = "/elastic", method = RequestMethod.POST)
    public ArrayList<Script> search_all(String SearchText,String username) throws IOException {
        System.out.println("SearchText"+SearchText);
        if(username!=null){
        recommendService.updateSearchHistory(username, SearchText);}
        else{
            recommendService.updateSearchHistory("default", SearchText);
        }
       return squeryService.executeSearchQuery(SearchText);
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


    //点击量排序
    @RequestMapping(value = "/clickSort", method = RequestMethod.POST)
    public ArrayList<Script> clickSort(String SearchText) throws IOException {
        return squeryService.searchByClick(SearchText);
    }


    //综合排序
    @RequestMapping(value = "/combinedRank", method = RequestMethod.POST)
    public ArrayList<Script> combinedRank(String SearchText) throws IOException {
        return squeryService.searchByAll(SearchText);
    }

    //筛选标签
    @RequestMapping(value = "/selectByTag", method = RequestMethod.POST)
    public ArrayList<Script> selectByTag(String SearchText, Integer a1, Integer a2, Integer a3, Integer a4) throws IOException {
        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(a1);
        ints.add(a2);
        ints.add(a3);
        ints.add(a4);
        return squeryService.searchByTags(SearchText, ints);
    }

    //食材搜索
    @RequestMapping(value = "/ingredients", method = RequestMethod.POST)
    public ArrayList<Script> ingredients(String SearchText) throws IOException {
        return ingredientSearchService.executeSearchQuery(SearchText);
    }

    //步骤搜索
    @RequestMapping(value = "/steps", method = RequestMethod.POST)
    public ArrayList<Script> steps(String SearchText) throws IOException {
        return stepsSearchService.executeSearchQuery(SearchText);
    }
}
