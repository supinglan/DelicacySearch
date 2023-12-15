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

    //相关搜索
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public List<String> recommend(String username) throws IOException {
        List<String> result = new ArrayList<>();
        if(username!=null){
            return recommendService.getRecommend(username);}
        else{
            return recommendService.getRecommend("default");
        }
    }

    //热搜
    @RequestMapping(value = "/hot", method = RequestMethod.POST)
    public ArrayList<String> hot() throws IOException {
        ArrayList<String> dishes = new ArrayList<>();
        dishes.add("红烧肉");
        dishes.add("红烧排骨");
        dishes.add("可乐鸡翅");
        dishes.add("糖醋排骨");
        dishes.add("水煮鱼");
        dishes.add("红烧鱼");
        dishes.add("鱼香肉丝");
        dishes.add("水煮肉片");
        dishes.add("鲫鱼豆腐汤");
        dishes.add("酸辣土豆丝");
        dishes.add("乌鸡汤");
        dishes.add("麻婆豆腐");
        dishes.add("鲫鱼汤");
        dishes.add("炸酱面");
        dishes.add("宫保鸡丁");
        dishes.add("红烧茄子");
        dishes.add("鱼香茄子");
        dishes.add("啤酒鸭");
        dishes.add("银耳汤");
        dishes.add("红烧牛肉");
        dishes.add("辣子鸡");
        dishes.add("牛肉炖土豆");
        dishes.add("香辣虾");
        dishes.add("红烧狮子头");
        dishes.add("小鸡炖蘑菇");
        dishes.add("糖醋里脊");
        dishes.add("土豆炖牛肉");
        dishes.add("红烧带鱼");
        dishes.add("大盘鸡");
        dishes.add("冬瓜汤");
        dishes.add("醋溜白菜");
        dishes.add("红烧鸡翅");
        dishes.add("东坡肉");
        dishes.add("罗宋汤");
        dishes.add("酸辣汤");
        dishes.add("排骨汤");
        dishes.add("毛血旺");
        dishes.add("泡椒凤爪");
        dishes.add("辣子鸡丁");
        dishes.add("椒盐虾");
        dishes.add("家常豆腐");
        dishes.add("蚂蚁上树");
        dishes.add("芹菜炒香干");
        dishes.add("冬瓜排骨汤");
        dishes.add("奥尔良烤翅");
        dishes.add("农家小炒肉");
        dishes.add("酱爆鸡丁");
        dishes.add("四喜丸子");
        dishes.add("凉拌黑木耳");
        dishes.add("酸辣白菜");
        dishes.add("麻辣小龙虾");
        dishes.add("冬瓜排骨汤");
        dishes.add("清蒸鲈鱼");
        dishes.add("孜然羊肉");
        Collections.shuffle(dishes);

        // Select the first 8 dishes
        return new ArrayList<>(dishes.subList(0, 8));
    }

}
