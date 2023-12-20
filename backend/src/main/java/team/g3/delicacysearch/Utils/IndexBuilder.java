package team.g3.delicacysearch.Utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.g3.delicacysearch.dao.FoodTripleMapper;
import team.g3.delicacysearch.model.FoodTriple;
import team.g3.delicacysearch.pojo.Script;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@Component
public class IndexBuilder {
    @Autowired
    FoodTripleMapper foodTripleMapper;
    public void buildIndex() throws Exception {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("120.55.14.3", 9200, "http")));

        GetIndexRequest getIndexRequest = new GetIndexRequest("script_index");
        boolean indexExists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (indexExists) {
            // 创建删除索引请求
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("script_index");
            // 发送删除索引请求
            restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            System.out.println("索引已存在并已删除：" + "script_index");
        } else {
            System.out.println("索引不存在：" + "script_index");
        }
        XContentBuilder mapping = XContentFactory.jsonBuilder();
        mapping.startObject();
        {
            mapping.startObject("properties");
            {
                // text_title字段使用IK分词器
                mapping.startObject("title");
                {
                    mapping.field("type", "text");
                    mapping.field("analyzer", "ik_max_word");
                }
                mapping.endObject();

                // Abstract字段使用IK分词器
                mapping.startObject("abstract");
                {
                    mapping.field("type", "text");
                    mapping.field("analyzer", "ik_max_word");
                }
                mapping.endObject();

                // ingredient字段使用IK分词器
                mapping.startObject("ingredient");
                {
                    mapping.field("type", "text");
                    mapping.field("analyzer", "ik_max_word");
                }
                mapping.endObject();

                // steps字段使用IK分词器
                mapping.startObject("steps");
                {
                    mapping.field("type", "text");
                    mapping.field("analyzer", "ik_max_word");
                }
                mapping.endObject();
            }
            mapping.endObject();
        }
        mapping.endObject();
        CreateIndexRequest request = new CreateIndexRequest("script_index");
        request.mapping(mapping);
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);


        GetIndexRequest getIndexRequest2 = new GetIndexRequest("pigg_test_pinyin");
        boolean indexExists2 = restHighLevelClient.indices().exists(getIndexRequest2, RequestOptions.DEFAULT);
        if (indexExists2) {
            // 创建删除索引请求
            DeleteIndexRequest deleteIndexRequest1 = new DeleteIndexRequest("pigg_test_pinyin");
            // 发送删除索引请求
            restHighLevelClient.indices().delete(deleteIndexRequest1, RequestOptions.DEFAULT);
            System.out.println("索引已存在并已删除：" + "pigg_test_pinyin");
        } else {
            System.out.println("索引不存在：" + "pigg_test_pinyin");
        }
        CreateIndexRequest request2 = new CreateIndexRequest("pigg_test_pinyin");
        request2.source(getIndexSettings(), XContentType.JSON);

        CreateIndexResponse response1 = restHighLevelClient.indices().create(request2, RequestOptions.DEFAULT);
        boolean acknowledged = response1.isAcknowledged();
        boolean shardsAcknowledged = response1.isShardsAcknowledged();
        if (acknowledged && shardsAcknowledged) {
            System.out.println("Index created successfully.");
        } else {
            System.out.println("Failed to create index.");
        }


        //下厨房
        testUtil.CreateIndex ();


        //美食天下
        int num = 1;
        for (int t = 12; t <= 700000; t++) {
            String url = "https://home.meishichina.com/recipe-" + t + ".html";
            try {
                Document document = Jsoup.parse(new URL(url), 30000);
                Elements elements = document.getElementsByClass("recipe_De_title");
                Element title;
                if (!elements.isEmpty()) {
                    title = elements.get(0);
                } else {
                    // 处理找不到元素的情况
                    System.out.println("页面" + t + "不存在");
                    continue;
                }
                String text_title = title.text();
                String source = "美食天下";
                String html_url = url;
                String pict_url = document.getElementsByClass("J_photo").get(0).select("img").attr("src");
                Element blockTxt1 = document.getElementById("block_txt1");
                String Abstract = (blockTxt1 != null) ? removeQuotes(blockTxt1.text()) : "";
                if(Abstract.isEmpty()) continue;
                Elements liElements = document.getElementsByClass("category_s1");
                ArrayList<String> ingredient = new ArrayList<>();
                for(int i = 0; i<liElements.size()-4;i++)
                {
                    if(!liElements.get(i).text().isEmpty()) {
                        String ingre=liElements.get(i).text();
                        ingredient.add(ingre);
                        FoodTriple foodTriple = new FoodTriple();
                        foodTriple.setSource(text_title);
                        foodTriple.setRelation("食材");
                        foodTriple.setTarget(ingre);
                        foodTripleMapper.insert(foodTriple);
                        System.out.println(text_title);
                        System.out.println(ingredient);
                    }
                }
                ArrayList<String> steps = new ArrayList<>();
                Elements elements1 = document.getElementsByClass("recipeStep_word noStep_word");
                for (Element element : elements1) {
                    steps.add(element.text());
                }
                ArrayList<String> tags = new ArrayList<>();
                for(int i = liElements.size()-4; i<liElements.size()-2;i++)
                {
                    if(!liElements.get(i).text().isEmpty())
                    {
                        String adder = judgeTags_meishitianxia(liElements.get(i).text());
                        if(adder != null) {
                            tags.add(adder);
                        }
                    }

                }
                Elements elements2 = document.getElementsByClass("vest");
                if(!elements2.isEmpty())
                {
                    //主tag
                    for (Element element : elements2) {
                        String adder = judgeTags_meishitianxia(element.text());
                      if(adder != null) {
                          tags.add(adder);
                      }
                    }
                    //内部tag

                }
                Random random = new Random();
                int min = 1;
                int max = 500;
                Integer click =  random.nextInt(max - min + 1) + min;
                //放入索引

                Script script = new Script(num, pict_url, html_url, text_title, Abstract, ingredient, steps, source, tags, click, 0);

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
                if(num>200000) break;


            } catch (HttpStatusException e){
                e.printStackTrace();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("索引创建完成！");
    }





    public static String judgeTags_meishitianxia(String tag)
    {
        //口味
        if(Objects.equals(tag, "微辣") || Objects.equals(tag, "中辣") || Objects.equals(tag, "超辣") || Objects.equals(tag, "麻辣") || Objects.equals(tag, "酸辣") || Objects.equals(tag, "甜辣") || Objects.equals(tag, "香辣")) return "辣";
        if(Objects.equals(tag, "咖喱")) return "咖喱";
        if(Objects.equals(tag, "蒜香")) return "蒜香";
        if(Objects.equals(tag, "酸甜")) return "酸甜";
        if(Objects.equals(tag, "奶香")) return "奶香";
        if(Objects.equals(tag, "孜然")) return "孜然";
        if(Objects.equals(tag, "鱼香")) return "鱼香";
        if(Objects.equals(tag, "五香")) return "五香";
        if(Objects.equals(tag, "清淡")) return "清淡";
        //烹饪方式
        if(Objects.equals(tag, "煎") || Objects.equals(tag, "蒸") || Objects.equals(tag, "炖") || Objects.equals(tag, "烧") || Objects.equals(tag, "炸") || Objects.equals(tag, "卤") || Objects.equals(tag, "干锅") || Objects.equals(tag, "火锅")) return tag;
        //场景
        if(Objects.equals(tag, "早餐") || Objects.equals(tag, "下午茶") || Objects.equals(tag, "二人世界")) return tag;
        if(Objects.equals(tag, "私房菜") || Objects.equals(tag, "西式宴请") || Objects.equals(tag, "中式宴请") || Objects.equals(tag, "热菜")) return "正餐";
        //种类
        if(Objects.equals(tag, "烘焙") || Objects.equals(tag, "汤羹") || Objects.equals(tag, "主食") || Objects.equals(tag, "小吃") || Objects.equals(tag, "素菜") || Objects.equals(tag, "凉菜")) return tag;
        if(Objects.equals(tag, "海鲜")) return "荤菜";
        return null;
    }



    public static String abstract_converter(String text)
    {
        String[] punctuations = {"。", "！", "？"};
        int lastIndex = -1;
        String input;
        for (String punctuation : punctuations) {
            int index = text.lastIndexOf(punctuation);
            if (index > lastIndex) {
                lastIndex = index;
            }
        }

        if (lastIndex != -1) {
            input = text.substring(0, lastIndex + 1);
        } else {
            input = "";
        }

        return input;
    }
    public static String removeQuotes(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        if ((input.startsWith("\"") && input.endsWith("\"")) || (input.startsWith("“") && input.endsWith("”"))) {
            return input.substring(1, input.length() - 1);
        }

        return input;
    }

    private static String getIndexSettings() {
        return "{\n" +
                "    \"settings\": {\n" +
                "        \"analysis\": {\n" +
                "            \"analyzer\": {\n" +
                "                \"pinyin_analyzer\": {\n" +
                "                    \"tokenizer\": \"my_pinyin\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"tokenizer\": {\n" +
                "                \"my_pinyin\": {\n" +
                "                    \"type\": \"pinyin\",\n" +
                "                    \"keep_first_letter\": true,\n" +
                "                    \"keep_separate_first_letter\": true,\n" +
                "                    \"keep_full_pinyin\": true,\n" +
                "                    \"remove_duplicated_term\": true\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"mappings\": {\n" +
                "        \"properties\": {\n" +
                "            \"name\": {\n" +
                "                \"type\": \"text\",\n" +
                "                \"analyzer\": \"standard\",\n" +
                "                \"fields\": {\n" +
                "                    \"pinyin\": {\n" +
                "                        \"type\": \"text\",\n" +
                "                        \"analyzer\": \"pinyin_analyzer\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
}
