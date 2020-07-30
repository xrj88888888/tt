package com.es;

import com.alibaba.fastjson.JSON;
import com.es.bean.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest
class EsApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Test
    void contextLoads() {
    }

    @Test
    void addIndex(){
        CreateIndexRequest request = new CreateIndexRequest("db2");
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(createIndexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void existIndex(){
        GetIndexRequest request = new GetIndexRequest("db");
        try {
            boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
            System.out.println(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void delIndex(){
        DeleteIndexRequest request = new DeleteIndexRequest("db");
        try {
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            System.out.println(delete.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addDoc(){
        User user = new User(3,null);
        IndexRequest indexRequest = new IndexRequest("db");
        indexRequest.id("3");
        indexRequest.source(JSON.toJSON(user),XContentType.JSON);

        try {
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(index.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void deleteDoc(){
        DeleteRequest deleteRequest = new DeleteRequest("db");
        deleteRequest.id("1");
        try {
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void query(){


        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("name", "Xi".toLowerCase());
        PrefixQueryBuilder prefixQueryBuilder2 = QueryBuilders.prefixQuery("tag", "Xi".toLowerCase());

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(prefixQueryBuilder);
        boolQueryBuilder.should(prefixQueryBuilder2);

        SearchSourceBuilder sb = new SearchSourceBuilder();
        SearchSourceBuilder ssb = sb.query(boolQueryBuilder);

        ssb.fetchSource();

        SearchRequest request = new SearchRequest("mygoods");
        request.source(ssb);
        try {
            SearchResponse result = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = result.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println(sourceAsMap.get("name"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
