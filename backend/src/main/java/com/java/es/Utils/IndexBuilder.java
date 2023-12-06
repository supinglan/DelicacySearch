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
            System.out.println("索引不存在：" +


                    "script_index");
        }
        CreateIndexRequest request = new CreateIndexRequest("script_index");
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
//        for (int t = 0; t <= 2000; t++) {
//            try {
//                String url = "https://www.shipuxiu.com/caipu/" + t + "/";
//                Document document = Jsoup.parse(new URL(url), 30000);
//                Elements elements = document.getElementsByClass("recipe-show");
//                Element title;
//                if (!elements.isEmpty()) {
//                    title = elements.get(0);
//                } else {
//                    // 处理找不到元素的情况
//                    System.out.println("页面" + t + "不存在");
//                    continue;
//                }
//                Element paragraphs = title.getElementsByTag("p").get(0);
//                String text_title = paragraphs.text();
//                String source = "食谱秀";
//                String html_url = url;
//                String pict_url = document.getElementsByClass("showLeft left").get(0).select("img").attr("src");
//                String Abstract = abstract_converter(document.getElementsByClass("desc description").get(0).text());
//                if (Abstract.isEmpty()) continue;
//                Elements liElements = document.select("div.material > ul > li.ingtbur");
//                ArrayList<String> ingredient = new ArrayList<>();
//                for (Element li : liElements) {
//                    if (!li.text().isEmpty()) {
//                        ingredient.add(li.text());
//                    }
//                }
//                ArrayList<String> steps = new ArrayList<>();
//                Elements elements1 = document.select("p.sstep");
//                for (Element element : elements1) {
//                    steps.add(element.text());
//                }
//                //放入索引
//                Script script = new Script(pict_url, html_url, text_title, Abstract, ingredient, steps, source);
//                IndexRequest request1 = new IndexRequest("script_index");
//                request1.id(Integer.toString(t));
//                request1.timeout(TimeValue.timeValueSeconds(1));
//                request1.timeout("1s");
//                request1.source(JSON.toJSONString(script), XContentType.JSON);
//                IndexResponse indexResponse = restHighLevelClient.index(request1, RequestOptions.DEFAULT);
//
//                System.out.println(indexResponse.toString());
//            }
//            catch (HttpStatusException e)
//            {
//                System.out.println("HTTP状态异常：");
//            }
//        }
        for (int t = 12; t <= 70000; t++) {
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
                //放入索引
                Script script = new Script(pict_url, html_url, text_title, Abstract, ingredient, steps, source);
                IndexRequest request1 = new IndexRequest("script_index");
                request1.id(Integer.toString(t + 5000));
                request1.timeout(TimeValue.timeValueSeconds(1));
                request1.timeout("1s");
                request1.source(JSON.toJSONString(script), XContentType.JSON);
                IndexResponse indexResponse = restHighLevelClient.index(request1, RequestOptions.DEFAULT);

                System.out.println(indexResponse.toString());
            }
            catch (HttpStatusException e) {
                // 处理HTTP状态异常
                System.out.println("HTTP状态异常：" + e.getStatusCode());

            }
        }

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
}
