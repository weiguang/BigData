package com.okayjam.bigdata.elasticsearch.service;

import com.okayjam.bigdata.elasticsearch.factory.TransportClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/14 15:58
 **/
public class TransportClientService {
    private TransportClient client = TransportClientFactory.getInstance().getTransportClient();

    /**
     *判断索引是否存在
     * @param index
     * @return
     */
    public boolean isIndexExist(String index) {
        return client.admin().indices().prepareExists(index).execute().actionGet().isExists();
    }


    /**
     * 新增索引
     * @param index
     * @return
     */
    public boolean addIndex(String index) {
        return isIndexExist(index)
                ? false
                : client.admin().indices().prepareCreate(index).execute().actionGet().isAcknowledged();
    }

    /**
     *
     * 删除索引
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) {
        return isIndexExist(index)
                ? client.admin().indices().prepareDelete(index).execute().actionGet().isAcknowledged()
                : false;
    }

    /**
     * 判断inde下指定type是否存在
     * @param index
     * @param type
     * @return
     */
    public boolean isTypeExist(String index, String type) {
        return isIndexExist(index)
                ? client.admin().indices().prepareTypesExists(index).setTypes(type).execute().actionGet().isExists()
                : false;
    }

    /**
     * 获取索引的所有数据，返回强size条
     */
    public SearchHits matchAllQuery(String index, int size) {
        QueryBuilder query = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(index).setQuery(query).setSize(size).execute().actionGet();
//        for (SearchHit searchHit : response.getHits()) {
//            String jsonStr = searchHit.getSourceAsString();
//            System.out.println(jsonStr);
//        }
        return response.getHits();
    }



}
