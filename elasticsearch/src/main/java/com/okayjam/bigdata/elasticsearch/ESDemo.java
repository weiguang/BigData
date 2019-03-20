package com.okayjam.bigdata.elasticsearch;

import com.okayjam.bigdata.elasticsearch.factory.RestHighLevelClientFactory;
import com.okayjam.bigdata.elasticsearch.factory.TransportClientFactory;
import com.okayjam.bigdata.elasticsearch.service.TransportClientService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/14 10:01
 **/
public class ESDemo {
    String esIndexName = "ops_container_waybill_info_rel_index";
    public static void main(String[] args) {
//        new ESDemo().testTransportClient();
        new ESDemo().testRestHighClient();
//        new ESDemo().testIndex() ;
//        new ESDemo().testHits() ;
    }

    public void testTransportClient() {
        Map<String, Object> params = new HashMap<>();
        String endtmStart = "2019-03-07 00:00";
        String endtmEnd = "2019-03-09 23:59";
        String removezonecode = "852RA";

//        params.put("limitTypeCode","T7");
//        params.put("srcCityCode","579");
//        params.put("destCityCode","LAX");
//        params.put("srcDistCode","579Y");
//        params.put("destAreaCode","USJFKYS");

        //查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("removezonecode",removezonecode));
        boolQueryBuilder.must(new RangeQueryBuilder("endtm").gte(endtmStart).lte(endtmEnd));

        TransportClient client = TransportClientFactory.getInstance().getTransportClient();

        SearchSourceBuilder ssb = new SearchSourceBuilder();

        SearchResponse searchResponse = client
                .prepareSearch(esIndexName).setSearchType(SearchType.DEFAULT)
                .setSize(5).setQuery(boolQueryBuilder).execute()
                .actionGet();
        System.out.println(searchResponse.getHits().totalHits);
        for (SearchHit searchHit : searchResponse.getHits()) {
            String jsonStr = searchHit.getSourceAsString();
            System.out.println(jsonStr);
        }
    }


    public void testRestHighClient() {
        String endtmStart = "2019-03-07 00:00";
        String endtmEnd = "2019-03-09 23:59";
        String removezonecode = "852RA";

        BoolQueryBuilder boolQueryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("removezonecode",removezonecode));
        boolQueryBuilder.must(new RangeQueryBuilder("endtm").gte(endtmStart).lte(endtmEnd));

        SearchSourceBuilder ssb = new SearchSourceBuilder();
        ssb.query(boolQueryBuilder).size(5);

        SearchRequest sr = new SearchRequest();
        sr.indices(esIndexName);
        sr.source(ssb);

        AggregationBuilder tb1 = AggregationBuilders.terms("src_dist_code_list").field("src_dist_code").size(10000);

        TermsAggregationBuilder tb2 = AggregationBuilders.terms("dest_area_code_list").field("dest_area_code").size(10000);
        TermsAggregationBuilder tb3 = AggregationBuilders.terms("src_city_code_list").field("src_city_code").size(10000);
        TermsAggregationBuilder tb4 = AggregationBuilders.terms("dest_city_code_list").field("dest_city_code").size(10000);
        TermsAggregationBuilder tb5 = AggregationBuilders.terms("limit_type_code_list").field("limit_type_code").size(10000);

        SumAggregationBuilder tbSum = AggregationBuilders.sum("meterage_weight_qty_sum").field("meterage_weight_qty");

        tb5.subAggregation(tbSum);
        tb4.subAggregation(tb5);
        tb3.subAggregation(tb4);
        tb2.subAggregation(tb3);
        tb1.subAggregation(tb2);

        ssb.aggregation(tb1);


        System.out.println(sr.source());

        RestHighLevelClient client = RestHighLevelClientFactory.getInstance().getHighLevelClient();
        try {
            SearchResponse response = client.search(sr);
            System.out.println(response.getHits().getTotalHits());

            for (SearchHit searchHit : response.getHits().getHits()) {
                String jsonStr = searchHit.getSourceAsString();
                System.out.println(jsonStr);
            }

            RestHighLevelClientFactory.getInstance().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void  testIndex() {
        TransportClientService ts = new TransportClientService();

        boolean ex;
//        ex = ts.isIndexExist(esIndexName);
//        System.out.println(ex);

        String addIndex = "test_index_jam";
        ts.addIndex(esIndexName);
        ex = ts.isIndexExist(esIndexName);
        System.out.println(ex);


//        ts.addIndex(addIndex);
//        ts.deleteIndex(esIndexName);
//        ex = ts.isIndexExist(addIndex);
//        System.out.println(ex);
    }

    public void testHits() {
        TransportClientService ts = new TransportClientService();
        SearchHits searchHits  =ts.matchAllQuery(esIndexName, 10);
        for (SearchHit searchHit : searchHits) {
            String jsonStr = searchHit.getSourceAsString();
            System.out.println(jsonStr);
        }

    }




}
