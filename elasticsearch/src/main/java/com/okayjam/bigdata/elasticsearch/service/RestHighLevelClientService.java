package com.okayjam.bigdata.elasticsearch.service;

import com.okayjam.bigdata.elasticsearch.factory.RestHighLevelClientFactory;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/14 15:58
 **/
public class RestHighLevelClientService {
    private RestHighLevelClient client = RestHighLevelClientFactory.getInstance().getHighLevelClient();

    /**
     *判断索引是否存在
     * @param index
     * @return
     */
    public boolean isIndexExist(String index) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        try {
//            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 新增索引
     * @param index
     * @return
     */
    public boolean addIndex(String index) {
        return false;
    }

    /**
     *
     * 删除索引
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) {
        return false;
    }

    /**
     * 判断inde下指定type是否存在
     * @param index
     * @param type
     * @return
     */
    public boolean isTypeExist(String index, String type) {
        return false;
    }

    /**
     * 获取索引的所有数据，返回强size条
     */
    public SearchHits matchAllQuery(String index, int size) {
        QueryBuilder query = QueryBuilders.matchAllQuery();
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        ssb.query(query);
        SearchRequest sr = new SearchRequest();
        sr.source(ssb);
        SearchResponse response = null;
        try {
            response = client.search(sr);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (SearchHit searchHit : response.getHits()) {
//            String jsonStr = searchHit.getSourceAsString();
//            System.out.println(jsonStr);
//        }
        return response.getHits();
    }



}
