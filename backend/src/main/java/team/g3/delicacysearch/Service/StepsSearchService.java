package team.g3.delicacysearch.Service;

import team.g3.delicacysearch.pojo.Script;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StepsSearchService {
        @Autowired
        public RestHighLevelClient elasticsearchClient;

        public ArrayList<Script> executeSearchQuery(String SearchText) throws IOException {
            if(SearchText.toLowerCase().contains("or") || SearchText.contains("-") || SearchText.contains("*") || SearchText.matches(".*[\"“”].*")) return preProcess(SearchText);
            //判断输入的搜索语句是否含有拼音
            boolean containsLetter = Pattern.matches(".*[a-zA-Z].*", SearchText);
            //若包含。我们按照自动补全的函数，获取一系列的汉字String，再搜索这些汉字String
            if(containsLetter)
            {
                List<String> results = getNames(SearchText);
                ArrayList<Script> total_results = new ArrayList<>();
                for (String result : results) {
                    ArrayList<Script> part_results = executeSearchQuery(result);
                    for (Script partResult : part_results) {
                        if (!total_results.contains(partResult)) {
                            total_results.add(partResult);
                        }
                    }
                }
                return total_results;
            }
            //汉字搜索
            else
            {
                ArrayList<Script> result = new ArrayList<>();
                try {
                    SearchRequest searchRequest = new SearchRequest("script_index");
                    MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                            .field("steps", 5.0F);  // 设置"steps"字段的权重为1.0
                    searchRequest.source().query(queryBuilder);
                    searchRequest.source().fetchSource(true);
                    searchRequest.source().sort("_score", SortOrder.DESC);
                    searchRequest.source().size(500);
                    SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                    SearchHits hits = searchResponse.getHits();
                    long hitnum = hits.getTotalHits().value;
                    if(hitnum == 0) System.out.println("无结果！");
                    for (SearchHit hit : hits) {
                        // 处理每个命中的文档
                        String url = hit.getSourceAsMap().get("html_url").toString();
                        String pic_url = hit.getSourceAsMap().get("pict_url").toString();
                        String title = hit.getSourceAsMap().get("title").toString();
                        String Abstract = hit.getSourceAsMap().get("abstract").toString();
                        String source = hit.getSourceAsMap().get("origin").toString();
                        Integer clicks = Integer.valueOf(hit.getSourceAsMap().get("clicks").toString());
                        ArrayList<String> steps = new ArrayList<>();
                        List<Object> stepsList = (List<Object>) hit.getSourceAsMap().get("steps");
                        if (stepsList != null) {
                            for (Object step : stepsList) {
                                if (step instanceof String) {
                                    steps.add((String) step);
                                }
                            }
                        }
                        ArrayList<String> ingredients = new ArrayList<>();
                        List<Object> ingreList = (List<Object>) hit.getSourceAsMap().get("ingredient");
                        if (ingreList != null) {
                            for (Object step : ingreList) {
                                if (step instanceof String) {
                                    ingredients.add((String) step);
                                }
                            }
                        }
                        ArrayList<String> tags = new ArrayList<>();
                        List<Object> tagLists = (List<Object>) hit.getSourceAsMap().get("tag");
                        if(tagLists != null)
                        {
                            for(Object tag : tagLists)
                            {
                                if(tag instanceof  String)
                                {
                                    tags.add((String)tag);
                                }
                            }
                        }
                        float score = hit.getScore();
                        // 在这里执行您希望的操作，比如打印或处理结果
                        System.out.println("菜名: " + title);
                        System.out.println("摘要:" + Abstract);
                        System.out.println(" ");
//                System.out.println("网页地址: " + url);
//                System.out.println("图片地址: " + pic_url);
                        Script ele = new Script(pic_url, url, title, Abstract, ingredients, steps, source, tags, clicks, score);
                        result.add(ele);
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        //汉字的自动补全功能，返回所有包含输入的标题
        public ArrayList<String> AutoFill(String SearchText)
        {
            ArrayList<String> result = new ArrayList<>();
            try {
                SearchRequest searchRequest = new SearchRequest("script_index");
                MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                        .field("steps", 5.0F);  // 设置"steps"字段的权重为1.0

                searchRequest.source().query(queryBuilder);
                searchRequest.source().fetchSource(true);
                searchRequest.source().sort("_score", SortOrder.DESC);
                searchRequest.source().size(200);
                SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                long hitnum = hits.getTotalHits().value;
                if(hitnum == 0) System.out.println("无结果！");
                for (SearchHit hit : hits) {
                    // 处理每个命中的文档
                    String title = hit.getSourceAsMap().get("title").toString();
                    //返回的title将不含自己本身
                    if (!result.contains(title) && !Objects.equals(title, SearchText)) {
                        result.add(title);
                    }
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        //拼音的自动补全功能，包含所有包含该拼音的标题
        public ArrayList<String> getNames(String SearchText) {
            ArrayList<String> result = new ArrayList<>();
            try {
                SearchRequest searchRequest = new SearchRequest("pigg_test_pinyin");
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                sourceBuilder.query(QueryBuilders.matchQuery("name.pinyin", SearchText));
                searchRequest.source(sourceBuilder);
                SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String name = hit.getSourceAsMap().get("name").toString();
                    if(!result.contains(name)) result.add(name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        //当输入为拼音时，先搜索包含其的标题，再对标题进行二次搜索
        public ArrayList<String> pinyin_all(String SearchText)
        {
            ArrayList<String> search_res = getNames(SearchText);
            ArrayList<String> total_res = new ArrayList<>();
            total_res.addAll(search_res);
            for (String searchRe : search_res) {
                ArrayList<String> corr_res = AutoFill(searchRe);
                for (int j = 0; j < corr_res.size(); j++) {
                    if (!total_res.contains(corr_res.get(j))) total_res.add(corr_res.get(j));
                }
            }
            return total_res;
        }

        //按照标签搜索
        public ArrayList<Script> searchByTags(String SearchText, ArrayList<Integer> searchOps) throws IOException {
            ArrayList<Script> results = executeSearchQuery(SearchText);
            ArrayList<String> tags = new ArrayList<>();
            //烹调方式
            switch(searchOps.get(0))
            {
                case 1: tags.add("煎"); break;
                case 2: tags.add("蒸"); break;
                case 3: tags.add("炖"); break;
                case 4: tags.add("烧"); break;
                case 5: tags.add("炸"); break;
                case 6: tags.add("卤"); break;
                case 7: tags.add("干锅"); break;
                case 8: tags.add("火锅"); break;
                case 0:
                default: break;
            }
            if(!tags.isEmpty())
            {
                results.removeIf(script -> !script.getTag().contains(tags.get(0)));
            }
            tags.clear();
            switch(searchOps.get(1))
            {
                case 1: tags.add("辣"); break;
                case 2: tags.add("咖喱"); break;
                case 3: tags.add("蒜香"); break;
                case 4: tags.add("酸甜"); break;
                case 5: tags.add("奶香"); break;
                case 6: tags.add("孜然"); break;
                case 7: tags.add("鱼香"); break;
                case 8: tags.add("五香"); break;
                case 9: tags.add("清淡"); break;
                case 0:
                default: break;
            }
            if(!tags.isEmpty())
            {
                results.removeIf(script -> !script.getTag().contains(tags.get(0)));
            }
            tags.clear();
            switch(searchOps.get(2))
            {
                case 1: tags.add( "早餐"); break;
                case 2: tags.add( "下午茶"); break;
                case 3: tags.add( "二人世界"); break;
                case 4: tags.add( "正餐"); break;
                case 0:
                default: break;
            }
            if(!tags.isEmpty())
            {
                results.removeIf(script -> !script.getTag().contains(tags.get(0)));
            }
            tags.clear();
            switch(searchOps.get(3))
            {
                case 1: tags.add("烘焙"); break;
                case 2: tags.add("汤羹"); break;
                case 3: tags.add("主食"); break;
                case 4: tags.add("小吃"); break;
                case 5: tags.add("荤菜"); break;
                case 6: tags.add("素菜"); break;
                case 7: tags.add("凉菜"); break;
                case 0:
                default: break;
            }
            if(!tags.isEmpty())
            {
                results.removeIf(script -> !script.getTag().contains(tags.get(0)));
            }
            return results;
        }

        //综合排序（相关度60% + 点击量40%）
        public ArrayList<Script> searchByAll(String SearchText) throws IOException {
            ArrayList<Script> results = executeSearchQuery(SearchText);
            // 按照 score 字段排序
            results.sort(Comparator.comparingDouble(Script::getScore).reversed());
            // 计算 click 排名
            ArrayList<Script> resultsByClicks = new ArrayList<>(results);
            resultsByClicks.sort(Comparator.comparing(Script::getClicks).reversed());
            // 存储综合排名值
            Map<Script, Double> combinedRanks = new HashMap<>();
            for (int i = 0; i < results.size(); i++) {
                double combinedRank = 0.6 * (i + 1); // 0-based index, so add 1 to avoid zero
                int clickRank = resultsByClicks.indexOf(results.get(i)) + 1; // 0-based index, so add 1 to avoid zero
                combinedRank += 0.4 * clickRank;
                combinedRanks.put(results.get(i), combinedRank); // 存储综合排名值
            }

            // 按照综合排名值从高到低排序
            results.sort(Comparator.comparingDouble(combinedRanks::get));
            return results;
        }

        //根据点击量排序
        public ArrayList<Script> searchByClick(String SearchText) throws IOException {
            ArrayList<Script> results = executeSearchQuery(SearchText);
            results.sort(Comparator.comparingInt(Script::getClicks).reversed()); // 根据 clicks 进行倒序排序
            return results;
        }


        private ArrayList<Script> preProcess(String SearchText) throws IOException {
            ArrayList<Script> searchResults = new ArrayList<>();
            if(SearchText.toLowerCase().contains("or"))
            {
                String[] subStrings;
                subStrings = SearchText.split("(?i)\\bor\\b");
                for (String subString : subStrings) {
                    searchResults.addAll(executeSearchQuery(subString));
                }
            }
            else if(SearchText.contains("-"))
            {
                String[] subStrings = SearchText.split("-");
                String result = subStrings[0];
                for (int i = 1; i < subStrings.length; i++) {
                    result = result.replace(subStrings[i], "");
                }
                searchResults.addAll(executeSearchQuery(result));
            }
            else if(SearchText.contains("*"))
            {
                String result = SearchText.replace("*", "");
                //当新字符串中包含拼音
                if(Pattern.matches(".*[a-zA-Z].*", result))
                {
                    ArrayList<String> searchers = pinyin_all(result);
                    for (String searcher : searchers) {
                        searchResults.addAll(executeSearchQuery(searcher));
                    }
                }
                else
                {
                    ArrayList<String> searchers = AutoFill(result);
                    searchResults.addAll(executeSearchQuery(result));
                    for (String searcher : searchers) {
                        searchResults.addAll(executeSearchQuery(searcher));
                    }
                }
            }
            else if(SearchText.matches(".*[\"“”].*"))
            {
                Pattern pattern = Pattern.compile("[\"“](.*?)[\"”]");
                Matcher matcher = pattern.matcher(SearchText);
                List<String> resultList = new ArrayList<>();
                while (matcher.find()) {
                    resultList.add(matcher.group(1));
                }
                SearchRequest searchRequest = new SearchRequest("script_index");
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                for (String str : resultList) {
                    boolQuery.must(
                            QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("steps", str))
                    );
                }
                searchRequest.source().query(boolQuery);
                searchRequest.source().fetchSource(true);
                searchRequest.source().sort("_score", SortOrder.DESC);
                SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                long hitnum = hits.getTotalHits().value;
                if(hitnum == 0) System.out.println("无结果！");
                for (SearchHit hit : hits) {
                    // 处理每个命中的文档
                    String url = hit.getSourceAsMap().get("html_url").toString();
                    String pic_url = hit.getSourceAsMap().get("pict_url").toString();
                    String title = hit.getSourceAsMap().get("title").toString();
                    String Abstract = hit.getSourceAsMap().get("abstract").toString();
                    String source = hit.getSourceAsMap().get("origin").toString();
                    Integer clicks = Integer.valueOf(hit.getSourceAsMap().get("clicks").toString());
                    ArrayList<String> steps = new ArrayList<>();
                    List<Object> stepsList = (List<Object>) hit.getSourceAsMap().get("steps");
                    if (stepsList != null) {
                        for (Object step : stepsList) {
                            if (step instanceof String) {
                                steps.add((String) step);
                            }
                        }
                    }
                    ArrayList<String> ingredients = new ArrayList<>();
                    List<Object> ingreList = (List<Object>) hit.getSourceAsMap().get("ingredient");
                    if (stepsList != null) {
                        for (Object step : ingreList) {
                            if (step instanceof String) {
                                ingredients.add((String) step);
                            }
                        }
                    }
                    ArrayList<String> tags = new ArrayList<>();
                    List<Object> tagLists = (List<Object>) hit.getSourceAsMap().get("tag");
                    if(tagLists != null)
                    {
                        for(Object tag : tagLists)
                        {
                            if(tag instanceof  String)
                            {
                                tags.add((String)tag);
                            }
                        }
                    }
                    float score = hit.getScore();
                    Script ele = new Script(pic_url, url, title, Abstract, ingredients, steps, source, tags, clicks, score);
                    searchResults.add(ele);
                }
            }
            return searchResults;
        }
    }

