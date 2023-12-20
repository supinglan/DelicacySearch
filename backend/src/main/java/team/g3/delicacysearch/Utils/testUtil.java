package team.g3.delicacysearch.Utils;


import com.alibaba.fastjson.JSON;
import team.g3.delicacysearch.dao.FoodTripleMapper;
import team.g3.delicacysearch.model.FoodTriple;
import team.g3.delicacysearch.pojo.Script;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

@Component
public class testUtil {
    static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
    static String referer = "https://www.xiachufang.com/";
    static Integer MAX_DOCUMENT = 200000;
    @Autowired
    static FoodTripleMapper foodTripleMapper;
    public static void CreateIndex () throws Exception {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("120.55.14.3", 9200, "http")));
        int num = 300000;
        ArrayList<String> categories = new ArrayList<>();
        HashSet<String> finalURLs = new HashSet<>();
        try {
            Document document = Jsoup.connect("https://www.xiachufang.com/category/").userAgent(userAgent).referrer(referer).get();
            Elements elements = document.getElementsByClass("cates-border-none");

            for (Element element : elements.select("a[href]")) {
                String href = element.attr("href");
                categories.add("https://www.xiachufang.com/" + href);
            }
            for(String url1 : categories)
            {
                String url;
                for(int page = 1; page<=25; page++) {
                    if(page == 1) url = url1;
                    else url = url1 + "?page=" + page;
                    try {
                        Document new_document = Jsoup.connect(url).userAgent(userAgent).referrer(referer).get();
                        Elements all_eles = new_document.getElementsByClass("info pure-u");//size = 25
                        int n = 0;
                        for (Element ele : all_eles) {
                            Element nameElement = ele.select(".name a").get(0); //size == 1
                            n++;
                            if (n > 20) break;
                            String href = "https://www.xiachufang.com/" + nameElement.attr("href");
                            if (!finalURLs.contains(href)) {
                                try {
                                    Document pageDocument = Jsoup.connect(href).userAgent(userAgent).referrer(referer).get();
                                    //对于每个合法URL，对页面进行操作
                                    String text_title;
                                    Elements titleElements = pageDocument.getElementsByClass("page-title");
                                    if (!titleElements.isEmpty()) {
                                        text_title = titleElements.get(0).text();
                                    }
                                    else continue;
                                    String source = "下厨房";
                                    String html_url = href;
                                    String pict_url = pageDocument.getElementsByClass("cover image expandable block-negative-margin").get(0).select("img").attr("src");
                                    String Abstract = pageDocument.getElementsByClass("desc mt30").get(0).text();
                                    ArrayList<String> ingredients = new ArrayList<>();
                                    Element ingsDiv = pageDocument.select("div.ings").first();
                                    if (ingsDiv != null) {
                                        Elements names = ingsDiv.select("td.name");
                                        for (Element name : names) {
                                            String ingredient = name.text();
                                            FoodTriple foodTriple = new FoodTriple();
                                            foodTriple.setSource(text_title);
                                            foodTriple.setRelation("食材");
                                            foodTriple.setTarget(ingredient);
                                            foodTripleMapper.insert(foodTriple);
                                            System.out.println(text_title);
                                            System.out.println(ingredient);
                                            ingredients.add(ingredient);
                                        }
                                    } else {
                                        System.out.println("未找到匹配的元素");
                                    }
                                    ArrayList<String> steps = new ArrayList<>();

                                    Elements stepsDiv = pageDocument.select("div.steps p.text");
                                    if (stepsDiv != null) {
                                        for (Element textElement : stepsDiv) {
                                            steps.add(textElement.text());
                                        }
                                    } else {
                                        System.out.println("未找到匹配的元素");
                                    }
                                    //tags
                                    ArrayList<String> tags = new ArrayList<>();
                                    Element element = pageDocument.select("div.recipe-cats").first();
                                    Elements aTags = element.select("a");
                                    for (Element aTag : aTags) {
                                        String converted = tagConverter(aTag.text());
                                        if(!Objects.equals(converted, "error") && !tags.contains(converted))  tags.add(converted);
                                    }

                                    Random random = new Random();
                                    int min = 1;
                                    int max = 500;
                                    Integer click =  random.nextInt(max - min + 1) + min;

                                    Script script = new Script(pict_url, html_url, text_title, Abstract, ingredients, steps, source, tags, click, 0);
                                    IndexRequest request1 = new IndexRequest("script_index");
                                    request1.id(Integer.toString(num));
                                    request1.timeout(TimeValue.timeValueSeconds(1));
                                    request1.timeout("1s");
                                    request1.source(JSON.toJSONString(script), XContentType.JSON);
                                    IndexResponse indexResponse = restHighLevelClient.index(request1, RequestOptions.DEFAULT);
                                    System.out.println(indexResponse.toString());

                                    String documents1 = "{ \"name\": \""+ text_title +"\" }"; // 替换为您要插入的数据
                                    IndexRequest request3 = new IndexRequest("pigg_test_pinyin");
                                    request3.id(Integer.toString(num++)); // 设置文档ID
                                    request3.source(documents1, XContentType.JSON);
                                    IndexResponse indexResponse4 = restHighLevelClient.index(request3, RequestOptions.DEFAULT);
                                    finalURLs.add(href);
                                    if(finalURLs.size()>=MAX_DOCUMENT) return;
                                    //插入
                                    System.out.println("插入成功");
                                }
                                catch (IndexOutOfBoundsException | NullPointerException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("inner error");
                                    int delaySeconds = 10; // 5秒的延迟，你可以根据实际情况调整
                                    Thread.sleep(delaySeconds * 1000); // 将秒数转换为毫秒数
                                }
                            }
                        }
                    } catch (Exception e) {
                        //
                        System.out.println("页面少于25");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("outer");
            int delaySeconds = 10; // 5秒的延迟，你可以根据实际情况调整
            Thread.sleep(delaySeconds * 1000); // 将秒数转换为毫秒数
        }
        System.out.println(finalURLs.size());
    }


    private static String tagConverter(String tag)
    {
        //调味
        if(Objects.equals(tag, "煎") || Objects.equals(tag, "蒸") || Objects.equals(tag, "炖") || Objects.equals(tag, "炸") || Objects.equals(tag, "卤") || Objects.equals(tag, "干锅") || Objects.equals(tag, "火锅")) return tag;
        else if(Objects.equals(tag, "红烧")) return "烧";
        else if(Objects.equals(tag, "辣") || Objects.equals(tag, "咖喱") || Objects.equals(tag, "蒜香") || Objects.equals(tag, "酸甜") || Objects.equals(tag, "奶香") || Objects.equals(tag, "孜然") || Objects.equals(tag, "鱼香") || Objects.equals(tag, "五香") || Objects.equals(tag, "清淡")) return tag;
        else if(Objects.equals(tag, "早餐") || Objects.equals(tag, "下午茶")) return tag;
        else if(Objects.equals(tag, "情人节")) return "二人世界";
        else if(Objects.equals(tag, "家常菜") || Objects.equals(tag, "快手菜") || Objects.equals(tag, "大鱼大肉") || Objects.equals(tag, "创意菜")) return "正餐";
        else if(Objects.equals(tag, "汤羹") || Objects.equals(tag, "烘焙") || Objects.equals(tag, "小吃") || Objects.equals(tag, "凉菜") || Objects.equals(tag, "素菜")) return tag;
        else if(Objects.equals(tag, "饭") || Objects.equals(tag, "饼") || Objects.equals(tag, "粥") || Objects.equals(tag, "饺子") || Objects.equals(tag, "馒头") || Objects.equals(tag, "三明治") || Objects.equals(tag, "馄饨") || tag.contains("面")) return "主食";
        else if(tag.contains("猪") || tag.contains("鸡") || tag.contains("牛") || tag.contains("羊") || tag.contains("鸭") || tag.contains("鸽") || tag.contains("肉") || tag.contains("蛙") || tag.contains("鱼") || tag.contains("虾") || tag.contains("贝") || tag.contains("蟹")) return "荤菜";
        else return "error";
    }

}
