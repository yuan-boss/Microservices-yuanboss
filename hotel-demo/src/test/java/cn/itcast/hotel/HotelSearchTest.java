package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-06 16:39
 **/

@SpringBootTest
public class HotelSearchTest {
    private RestHighLevelClient client;

    @Autowired
    private IHotelService hotelService;

    @BeforeEach
    void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://175.178.151.38:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }

    @Test
    void testMatchAll() throws IOException {
        //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4.解析响应
        handleResponse(response);
    }

    //全文检索查询（match，multi_match)
    @Test
    void testMatch() throws IOException {
        //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        //参数：查询字段，查询条件
       // request.source().query(QueryBuilders.matchQuery("all","如家"));
        //参数：查询条件，查询字段，查询字段...
        request.source().query(QueryBuilders.multiMatchQuery("如家","name","brand"));
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleResponse(response);
    }

    //精确查询
    @Test
    void testTermRange() throws IOException {
        //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        //精确值 Term 查询
        //request.source().query(QueryBuilders.termQuery("city","上海"));
        //范围 range 查询
        request.source().query(QueryBuilders.rangeQuery("price").gte(100).lt(150));
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleResponse(response);
    }

    //复合查询  bool query
    @Test
    void testBool() throws IOException {
        //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        //复合 bool 查询
        //2.1准备BoolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //2.2添加精确查询 term
        boolQuery.must(QueryBuilders.termQuery("city","上海"));
        //2.3添加range
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(100).lte(150));


        request.source().query(boolQuery);
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleResponse(response);
    }

    //排序分页处理
    @Test
    void testPageSorg() throws IOException {
        //页码
        int page = 1,size = 5;
        //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        //2.1 query
        request.source().query(QueryBuilders.matchAllQuery());
        //2.2 排序 sort
        request.source().sort("price", SortOrder.ASC);

        //2.3 分页 from、size
        request.source().from((page-1) * size).size(5);
        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleResponse(response);
    }

    //高亮处理
    @Test
    void testHighLight() throws IOException {
        //页码
        int page = 1,size = 5;
        //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        //2.1 query
        request.source().query(QueryBuilders.matchQuery("all","如家"));
        //注意：如果高亮处理多个字段，最后一个字段的样式处理会覆盖前面的字段样式
        request.source().highlighter(new HighlightBuilder()
                .field("brand").requireFieldMatch(false).preTags("<h1>").postTags("</h1>")
                .field("name").requireFieldMatch(false).preTags("<em>").postTags("</em")
                );
        //2.2高亮

        //3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //4.高亮结果解析

        handleResponseHighLight(response);
    }

    private void handleResponse(SearchResponse response) {
        //4.解析响应
        SearchHits searchHits = response.getHits();
        //4.1获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //4.2文档数组
        SearchHit[] hits = searchHits.getHits();
        //4.3 遍历
        for (SearchHit hit : hits) {
            //获取文档source
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }
    private void handleResponseHighLight(SearchResponse response) {
        //4.解析响应
        SearchHits searchHits = response.getHits();
        //4.1获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //4.2文档数组
        SearchHit[] hits = searchHits.getHits();
        //4.3 遍历
        for (SearchHit hit : hits) {
            //获取文档source
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields)){
                //根据字段名获取高亮结果
                HighlightField name = highlightFields.get("name");
                if (name != null){
                    //获取高亮值
                    String name1 = name.getFragments()[0].string();
                    //覆盖非高亮结果
                    hotelDoc.setName(name1);
                }
                HighlightField brand = highlightFields.get("brand");
                if (brand != null){
                    //获取高亮值
                    String brand1 = brand.getFragments()[0].string();
                    //覆盖非高亮结果
                    hotelDoc.setBrand(brand1);
                }

            }
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }
}
