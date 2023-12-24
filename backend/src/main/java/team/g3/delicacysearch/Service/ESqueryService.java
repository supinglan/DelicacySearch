package team.g3.delicacysearch.Service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.index.query.*;
import team.g3.delicacysearch.pojo.Script;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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
public class ESqueryService {
    @Autowired
    public RestHighLevelClient elasticsearchClient;
    private final static String index_source = "script_index";
    private final static String pinyin_source = "pinyin_index";

    //"test_kg""test_kg_pingyin"


    //汉字的自动补全功能，返回所有包含输入的标题
    public ArrayList<String> AutoFill(String SearchText) {
        ArrayList<String> result = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index_source);
            MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                    .field("title", 10.0F)  // 设置"title"字段的权重为3.0
                    .field("abstract", 2.0F)  // 设置"abstract"字段的权重为2.0
                    .field("ingredient", 1.5F)  // 设置"ingredient"字段的权重为1.5
                    .field("steps", 1.0F)  // 设置"steps"字段的权重为1.0
                    .field("origin", 0.5F);
            searchRequest.source().query(queryBuilder);
            searchRequest.source().fetchSource(true);
            searchRequest.source().sort("_score", SortOrder.DESC);
            searchRequest.source().size(200);
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            long hitnum = hits.getTotalHits().value;
            if (hitnum == 0) System.out.println("无结果！");
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
            SearchRequest searchRequest = new SearchRequest(pinyin_source);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery("name.pinyin", SearchText));
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                String name = hit.getSourceAsMap().get("name").toString();
                if (!result.contains(name)) result.add(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //当输入为拼音时，先搜索包含其的标题，再对标题进行二次搜索
    public ArrayList<String> pinyin_all(String SearchText) {
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

    public ArrayList<Script> search(String SearchText, ArrayList<Integer> searchOps, Integer type, Integer sortType) throws IOException {
        if (SearchText.toLowerCase().contains("or") || SearchText.contains("-") || SearchText.contains("*") || SearchText.matches(".*[\"“”].*"))
            return preProcess(SearchText, type, searchOps, sortType);
        //判断输入的搜索语句是否含有拼音
        boolean containsLetter = Pattern.matches(".*[a-zA-Z].*", SearchText);
        //若包含。我们按照自动补全的函数，获取一系列的汉字String，再搜索这些汉字String
        if (containsLetter) {
            List<String> results = getNames(SearchText);

            String result = results.get(0);
            ArrayList<Integer> list = new ArrayList<>();
            list.add(0);list.add(0);list.add(0);list.add(0);
            ArrayList<Script> total_results = search(result, list ,type, 0);

            switch (sortType) {
                case 0:
                    return searchByAll(total_results);
                case 1:
                    return total_results;
                case 2:
                    return searchByClick(total_results);
                default:
                    return null;
            }
        }
        BoolQueryBuilder boolQuery = boolQureyBuild(type, searchOps, SearchText);
        switch (sortType) {
            case 0:
                return searchByAll(gainResults(boolQuery));
            case 1:
                return gainResults(boolQuery);
            case 2:
                return searchByClick(gainResults(boolQuery));
            default:
                return null;
        }
    }


    public ArrayList<Script> search(String SearchText) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);list.add(0);list.add(0);list.add(0);
        return search(SearchText, list,0,0);
    }



    //综合排序（相关度80% + 点击量20%）
    public ArrayList<Script> searchByAll(ArrayList<Script> results) throws IOException {
        // 按照 score 字段排序
        results.sort(Comparator.comparingDouble(Script::getScore).reversed());
        // 计算 click 排名
        ArrayList<Script> resultsByClicks = new ArrayList<>(results);
        resultsByClicks.sort(Comparator.comparing(Script::getClicks).reversed());
        // 存储综合排名值
        Map<Script, Double> combinedRanks = new HashMap<>();
        for (int i = 0; i < results.size(); i++) {
            double combinedRank = 0.8 * (i + 1); // 0-based index, so add 1 to avoid zero
            int clickRank = resultsByClicks.indexOf(results.get(i)) + 1; // 0-based index, so add 1 to avoid zero
            combinedRank += 0.2 * clickRank;
            combinedRanks.put(results.get(i), combinedRank); // 存储综合排名值
        }

        // 按照综合排名值从高到低排序
        results.sort(Comparator.comparingDouble(combinedRanks::get));
        return results;
    }

    //根据点击量排序
    public ArrayList<Script> searchByClick(ArrayList<Script> results) throws IOException {
        results.sort(Comparator.comparingInt(Script::getClicks).reversed()); // 根据 clicks 进行倒序排序
        return results;
    }

    //根据相关度排序
    public ArrayList<Script> searchByScore(ArrayList<Script> results) throws IOException {
        results.sort(Comparator.comparingDouble(Script::getScore).reversed());
        return results;
    }


    //构造查询条件
    private BoolQueryBuilder boolQureyBuild(Integer type, ArrayList<Integer> searchOps, String SearchText) {
        MultiMatchQueryBuilder queryBuilder;
        if (type == 0) {
            queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                    .field("title", 10.0F)  // 设置"title"字段的权重为5.0
                    .field("abstract", 2.0F)  // 设置"abstract"字段的权重为2.0
                    .field("ingredient", 1.5F)  // 设置"ingredient"字段的权重为1.5
                    .field("steps", 1.0F)  // 设置"steps"字段的权重为1.0
                    .field("origin", 0.5F);
        } else if (type == 1) {
            queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                    .field("ingredient", 5.0F);  // 设置"ingredient"字段的权重为1.5;
        } else if (type == 2) {
            queryBuilder = QueryBuilders.multiMatchQuery(SearchText)
                    .field("steps", 5.0F);  // 设置"steps"字段的权重为1.0
        } else return null;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(queryBuilder);
        ArrayList<String> tags = new ArrayList<>();
        //烹调方式
        switch (searchOps.get(0)) {
            case 1:
                tags.add("煎");
                break;
            case 2:
                tags.add("蒸");
                break;
            case 3:
                tags.add("炖");
                break;
            case 4:
                tags.add("烧");
                break;
            case 5:
                tags.add("炸");
                break;
            case 6:
                tags.add("卤");
                break;
            case 7:
                tags.add("干锅");
                break;
            case 8:
                tags.add("火锅");
                break;
            case 0:
            default:
                break;
        }
        switch (searchOps.get(1)) {
            case 1:
                tags.add("辣");
                break;
            case 2:
                tags.add("咖喱");
                break;
            case 3:
                tags.add("蒜香");
                break;
            case 4:
                tags.add("酸甜");
                break;
            case 5:
                tags.add("奶香");
                break;
            case 6:
                tags.add("孜然");
                break;
            case 7:
                tags.add("鱼香");
                break;
            case 8:
                tags.add("五香");
                break;
            case 9:
                tags.add("清淡");
                break;
            case 0:
            default:
                break;
        }


        switch (searchOps.get(2)) {
            case 1:
                tags.add("早餐");
                break;
            case 2:
                tags.add("下午茶");
                break;
            case 3:
                tags.add("二人世界");
                break;
            case 4:
                tags.add("正餐");
                break;
            case 0:
            default:
                break;
        }

        switch (searchOps.get(3)) {
            case 1:
                tags.add("烘焙");
                break;
            case 2:
                tags.add("汤羹");
                break;
            case 3:
                tags.add("主食");
                break;
            case 4:
                tags.add("小吃");
                break;
            case 5:
                tags.add("荤菜");
                break;
            case 6:
                tags.add("素菜");
                break;
            case 7:
                tags.add("凉菜");
                break;
            case 0:
            default:
                break;
        }
        for(String tag: tags)
            boolQuery.must(QueryBuilders.termsQuery("tag.keyword", tag));


        return boolQuery;
    }

    public Script gainResultById(int id) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index_source);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQuery = QueryBuilders.termQuery("_id", id);
        sourceBuilder.query(termQuery);
        sourceBuilder.fetchSource(true);

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();

        if (hits.getTotalHits().value == 0) {
            System.out.println("无结果！");
            return null;
        }

        SearchHit hit = hits.getAt(0);

        // 处理命中的文档
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
        if (tagLists != null) {
            for (Object tag : tagLists) {
                if (tag instanceof String) {
                    tags.add((String) tag);
                }
            }
        }

        float score = hit.getScore();

        // 在这里执行您希望的操作，比如打印或处理结果
        System.out.println("菜名: " + title);
        System.out.println("摘要:" + Abstract);
        System.out.println(" ");
        // 创建 Script 对象并返回
        return new Script(id, pic_url, url, title, Abstract, ingredients, steps, source, tags, clicks, score);
    }

    //根据查询条件获取查询结果
    private ArrayList<Script> gainResults(BoolQueryBuilder boolQueryBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index_source);
        ArrayList<Script> result = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        searchRequest.source().fetchSource(true);
        searchRequest.source().sort("_score", SortOrder.DESC);
        searchRequest.source().size(500);
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        long hitnum = hits.getTotalHits().value;
        if (hitnum == 0) System.out.println("无结果！");
        for (SearchHit hit : hits) {
            // 处理每个命中的文档
            Integer id = Integer.parseInt(hit.getId());

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
            if (tagLists != null) {
                for (Object tag : tagLists) {
                    if (tag instanceof String) {
                        tags.add((String) tag);
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
            Script ele = new Script(id, pic_url, url, title, Abstract, ingredients, steps, source, tags, clicks, score);
            result.add(ele);
        }
        return result;
    }


    private ArrayList<Script> preProcess(String SearchText, Integer type, ArrayList<Integer> searchOps, Integer sortType) throws IOException {
        ArrayList<Script> searchResults = new ArrayList<>();
        if (SearchText.toLowerCase().contains("or")) {
            String[] parts = SearchText.split("(?i)\\bor\\b");
            String firstPart = parts[0];
            String secondPart = parts[1];
            BoolQueryBuilder boolQueryBuilder1 = boolQureyBuild(type, searchOps, firstPart);
            searchResults.addAll(gainResults(boolQueryBuilder1));
            BoolQueryBuilder boolQueryBuilder2 = boolQureyBuild(type, searchOps, secondPart);
            searchResults.addAll(gainResults(boolQueryBuilder2));
            switch (sortType) {
                case 0:
                    return searchByAll(searchResults);
                case 1:
                    return searchByScore(searchResults);
                case 2:
                    return searchByClick(searchResults);
                default:
                    return null;
            }
        } else if (SearchText.contains("*")) {
            String result = SearchText.replace("*", "");
            BoolQueryBuilder boolQueryBuilder1 = boolQureyBuild(type, searchOps, result);
            searchResults.addAll(gainResults(boolQueryBuilder1));
            switch (sortType) {
                case 0:
                    return searchByAll(searchResults);
                case 1:
                    return searchByScore(searchResults);
                case 2:
                    return searchByClick(searchResults);
                default:
                    return null;
            }
        } else if (SearchText.contains("-")) {
            String[] parts = SearchText.split("-");
            String firstPart = parts[0];
            String secondPart = parts[1];
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            // 添加“must”子句以确保结果中包含第一个部分
            if (type == 0) {
                boolQuery.must(QueryBuilders.multiMatchQuery(firstPart, "title", "abstract", "ingredient", "steps", "origin")
                        .field("title", 10.0F)
                        .field("abstract", 2.0F)
                        .field("ingredient", 1.5F)
                        .field("steps", 1.0F)
                        .field("origin", 0.5F));
            } else if (type == 1) {
                boolQuery.must(QueryBuilders.multiMatchQuery(firstPart, "ingredient")
                        .field("ingredient", 5.0F));
            } else if (type == 2) {
                boolQuery.must(QueryBuilders.multiMatchQuery(firstPart, "steps")
                        .field("steps", 5.0F));
            }

            // 添加“must_not”子句以确保结果中不包含第二个部分
            if (type == 0)
                boolQuery.mustNot(QueryBuilders.multiMatchQuery(secondPart, "title", "abstract", "ingredient", "steps", "origin"));
            else if (type == 1)
                boolQuery.mustNot(QueryBuilders.multiMatchQuery(secondPart, "ingredient"));
            else boolQuery.mustNot(QueryBuilders.multiMatchQuery(secondPart, "steps"));

            ArrayList<String> tags = new ArrayList<>();
            //烹调方式
            switch (searchOps.get(0)) {
                case 1:
                    tags.add("煎");
                    break;
                case 2:
                    tags.add("蒸");
                    break;
                case 3:
                    tags.add("炖");
                    break;
                case 4:
                    tags.add("烧");
                    break;
                case 5:
                    tags.add("炸");
                    break;
                case 6:
                    tags.add("卤");
                    break;
                case 7:
                    tags.add("干锅");
                    break;
                case 8:
                    tags.add("火锅");
                    break;
                case 0:
                default:
                    break;
            }
            switch (searchOps.get(1)) {
                case 1:
                    tags.add("辣");
                    break;
                case 2:
                    tags.add("咖喱");
                    break;
                case 3:
                    tags.add("蒜香");
                    break;
                case 4:
                    tags.add("酸甜");
                    break;
                case 5:
                    tags.add("奶香");
                    break;
                case 6:
                    tags.add("孜然");
                    break;
                case 7:
                    tags.add("鱼香");
                    break;
                case 8:
                    tags.add("五香");
                    break;
                case 9:
                    tags.add("清淡");
                    break;
                case 0:
                default:
                    break;
            }


            switch (searchOps.get(2)) {
                case 1:
                    tags.add("早餐");
                    break;
                case 2:
                    tags.add("下午茶");
                    break;
                case 3:
                    tags.add("二人世界");
                    break;
                case 4:
                    tags.add("正餐");
                    break;
                case 0:
                default:
                    break;
            }

            switch (searchOps.get(3)) {
                case 1:
                    tags.add("烘焙");
                    break;
                case 2:
                    tags.add("汤羹");
                    break;
                case 3:
                    tags.add("主食");
                    break;
                case 4:
                    tags.add("小吃");
                    break;
                case 5:
                    tags.add("荤菜");
                    break;
                case 6:
                    tags.add("素菜");
                    break;
                case 7:
                    tags.add("凉菜");
                    break;
                case 0:
                default:
                    break;
            }
            for(String tag: tags)
                boolQuery.must(QueryBuilders.termsQuery("tag.keyword", tag));
            switch (sortType) {
                case 0:
                    return searchByAll(gainResults(boolQuery));
                case 1:
                    return searchByScore(gainResults(boolQuery));
                case 2:
                    return searchByClick(gainResults(boolQuery));
                default:
                    return null;
            }
        }
        else if (SearchText.matches(".*[\"“”].*")) {
            Pattern pattern = Pattern.compile("[\"“](.*?)[\"”]");
            Matcher matcher = pattern.matcher(SearchText);
            List<String> resultList = new ArrayList<>();
            while (matcher.find()) {
                resultList.add(matcher.group(1));
            }

            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (type == 0)
                for (String str : resultList) {
                    boolQuery.must(
                            QueryBuilders.boolQuery()
                                    .should(QueryBuilders.matchPhraseQuery("title", str))
                                    .should(QueryBuilders.matchPhraseQuery("abstract", str))
                                    .should(QueryBuilders.matchPhraseQuery("ingredient", str))
                                    .should(QueryBuilders.matchPhraseQuery("steps", str))
                                    .should(QueryBuilders.matchPhraseQuery("origin", str))
                    );
                }
            else if (type == 1)
                for (String str : resultList) {
                    boolQuery.must(
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.matchPhraseQuery("ingredient", str))
                    );
                }
            else
                for (String str : resultList) {
                    boolQuery.must(
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.matchPhraseQuery("steps", str))
                    );
                }
            ArrayList<String> tags = new ArrayList<>();
            //烹调方式
            switch (searchOps.get(0)) {
                case 1:
                    tags.add("煎");
                    break;
                case 2:
                    tags.add("蒸");
                    break;
                case 3:
                    tags.add("炖");
                    break;
                case 4:
                    tags.add("烧");
                    break;
                case 5:
                    tags.add("炸");
                    break;
                case 6:
                    tags.add("卤");
                    break;
                case 7:
                    tags.add("干锅");
                    break;
                case 8:
                    tags.add("火锅");
                    break;
                case 0:
                default:
                    break;
            }
            switch (searchOps.get(1)) {
                case 1:
                    tags.add("辣");
                    break;
                case 2:
                    tags.add("咖喱");
                    break;
                case 3:
                    tags.add("蒜香");
                    break;
                case 4:
                    tags.add("酸甜");
                    break;
                case 5:
                    tags.add("奶香");
                    break;
                case 6:
                    tags.add("孜然");
                    break;
                case 7:
                    tags.add("鱼香");
                    break;
                case 8:
                    tags.add("五香");
                    break;
                case 9:
                    tags.add("清淡");
                    break;
                case 0:
                default:
                    break;
            }


            switch (searchOps.get(2)) {
                case 1:
                    tags.add("早餐");
                    break;
                case 2:
                    tags.add("下午茶");
                    break;
                case 3:
                    tags.add("二人世界");
                    break;
                case 4:
                    tags.add("正餐");
                    break;
                case 0:
                default:
                    break;
            }

            switch (searchOps.get(3)) {
                case 1:
                    tags.add("烘焙");
                    break;
                case 2:
                    tags.add("汤羹");
                    break;
                case 3:
                    tags.add("主食");
                    break;
                case 4:
                    tags.add("小吃");
                    break;
                case 5:
                    tags.add("荤菜");
                    break;
                case 6:
                    tags.add("素菜");
                    break;
                case 7:
                    tags.add("凉菜");
                    break;
                case 0:
                default:
                    break;
            }
            for(String tag: tags)
                boolQuery.must(QueryBuilders.termsQuery("tag.keyword", tag));
            switch (sortType) {
                case 0:
                    return searchByAll(gainResults(boolQuery));
                case 1:
                    return searchByScore(gainResults(boolQuery));
                case 2:
                    return searchByClick(gainResults(boolQuery));
                default:
                    return null;
            }
        } else return searchResults;
    }
}
