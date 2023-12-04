package com.java.es.Utils;

import com.alibaba.fastjson.JSON;
import com.java.es.pojo.Script;
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
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

@Component
public class IndexBuilder {
    public static void main(String[] Args) throws IOException {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        //建立拼音索引
//        GetIndexRequest getIndexRequest1 = new GetIndexRequest("pigg_test_pinyin");
//        boolean indexExists1 = restHighLevelClient.indices().exists(getIndexRequest1, RequestOptions.DEFAULT);
//        if (indexExists1) {
//            // 创建删除索引请求
//            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("pigg_test_pinyin");
//            // 发送删除索引请求
//            restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//            System.out.println("索引已存在并已删除：" + "pigg_test_pinyin");
//        } else {
//            System.out.println("索引不存在：" + "pigg_test_pinyin");
//        }
//        CreateIndexRequest request2 = new CreateIndexRequest("pigg_test_pinyin");
//        request2.source(getIndexSettings(), XContentType.JSON);
//        CreateIndexResponse response3 = restHighLevelClient.indices().create(request2, RequestOptions.DEFAULT);
//        boolean acknowledged = response3.isAcknowledged();
//        boolean shardsAcknowledged = response3.isShardsAcknowledged();
//
//        if (acknowledged && shardsAcknowledged) {
//            System.out.println("Index created successfully.");
//        } else {
//            System.out.println("Failed to create index.");
//        }
//
//
//        //建立食谱索引
//        GetIndexRequest getIndexRequest = new GetIndexRequest("script_index");
//        boolean indexExists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
//        if (indexExists) {
//            // 创建删除索引请求
//            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("script_index");
//            // 发送删除索引请求
//            restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//            System.out.println("索引已存在并已删除：" + "script_index");
//        } else {
//            System.out.println("索引不存在：" + "script_index");
//        }
//        CreateIndexRequest request = new CreateIndexRequest("script_index");
//        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
//        System.out.println(response);


        for (int t = 2001; t <= 5000; t++) {
            try {
                String url = "https://www.shipuxiu.com/caipu/" + t + "/";
                Document document = Jsoup.parse(new URL(url), 30000);
                Elements elements = document.getElementsByClass("recipe-show");
                Element title;
                if (!elements.isEmpty()) {
                    title = elements.get(0);
                } else {
                    // 处理找不到元素的情况
                    System.out.println("页面" + t + "不存在");
                    continue;
                }
                Element paragraphs = title.getElementsByTag("p").get(0);
                String text_title = paragraphs.text();
                String source = "食谱秀";
                String html_url = url;
                String pict_url = document.getElementsByClass("showLeft left").get(0).select("img").attr("src");
                String Abstract = abstract_converter(document.getElementsByClass("desc description").get(0).text());
                if (Abstract.isEmpty()) continue;
                Elements liElements = document.select("div.material > ul > li.ingtbur");
                ArrayList<String> ingredient = new ArrayList<>();
                for (Element li : liElements) {
                    if (!li.text().isEmpty()) {
                        ingredient.add(li.text());
                    }
                }
                ArrayList<String> steps = new ArrayList<>();
                Elements elements1 = document.select("p.sstep");
                for (Element element : elements1) {
                    steps.add(element.text());
                }
                //放入索引
                Script script = new Script(pict_url, html_url, text_title, Abstract, ingredient, steps, source);
                //向食谱索引插入数据
                IndexRequest request1 = new IndexRequest("script_index");
                request1.id(Integer.toString(t));

                request1.timeout(TimeValue.timeValueSeconds(1));
                request1.timeout("1s");
                request1.source(JSON.toJSONString(script), XContentType.JSON);
                IndexResponse indexResponse = restHighLevelClient.index(request1, RequestOptions.DEFAULT);
                //
                IndexRequest request4 = new IndexRequest("pigg_test_pinyin");
                request4.id(Integer.toString(t));
                request4.timeout(TimeValue.timeValueSeconds(1));
                request4.timeout("1s");
                String name = "{ \"name\": \""+ text_title + "\" }";
                request4.source(name, XContentType.JSON);
                IndexResponse indexResponse2 = restHighLevelClient.index(request4, RequestOptions.DEFAULT);
                System.out.println(indexResponse.toString());
                System.out.println();
            }
            catch (Exception e)
            {
                System.out.println("HTTP状态异常：");
            }
        }
        for (int t = 5001; t <= 10000; t++) {
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
                for (Element li : liElements) {
                    if (!li.text().isEmpty()) {
                        ingredient.add(li.text());
                    }
                }
                ArrayList<String> steps = new ArrayList<>();
                Elements elements1 = document.getElementsByClass("recipeStep_word noStep_word");
                for (Element element : elements1) {
                    steps.add(element.text());
                }
                IndexRequest request4 = new IndexRequest("pigg_test_pinyin");
                request4.id(Integer.toString(t+10000));
                request4.timeout(TimeValue.timeValueSeconds(1));
                request4.timeout("1s");
                String name = "{ \"name\": \""+ text_title + "\" }";
                request4.source(name, XContentType.JSON);
                IndexResponse indexResponse2 = restHighLevelClient.index(request4, RequestOptions.DEFAULT);
                //放入索引
                Script script = new Script(pict_url, html_url, text_title, Abstract, ingredient, steps, source);
                IndexRequest request1 = new IndexRequest("script_index");
                request1.id(Integer.toString(t + 10000));
                request1.timeout(TimeValue.timeValueSeconds(1));
                request1.timeout("1s");
                request1.source(JSON.toJSONString(script), XContentType.JSON);
                IndexResponse indexResponse = restHighLevelClient.index(request1, RequestOptions.DEFAULT);

                System.out.println(indexResponse.toString());
            }
            catch (Exception e) {
                // 处理HTTP状态异常
                System.out.println("状态异常：");

            }
        }
        System.out.println("索引创建完成！");
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
