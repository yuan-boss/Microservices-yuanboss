package cn.itcast.hotel;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cn.itcast.hotel.constants.HotelConstants.MAPPING_TEMPLATE;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-06 16:39
 **/
public class HotelIndexTest {
    private RestHighLevelClient client;


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
    void testInit(){
        System.out.println(client);
    }

    //创建索引库
    @Test
    void createHotelIndex() throws IOException {
        //1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");

        //2.准备请求的参数:DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        //3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }
    //删除索引库
    @Test
    void deleteHotelIndex() throws IOException {
        //创建Request对象
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        //发起请求
        client.indices().delete(request,RequestOptions.DEFAULT);
    }

    //判断索引库是否存在
    @Test
    void testExistsHotelIndex() throws IOException {
        //创建Request对象
        GetIndexRequest request = new GetIndexRequest("hotel");
        //发起请求
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        //输出
        System.out.println(exists);
    }
}
