package com.okayjam.bigdata.elasticsearch.factory;


import com.okayjam.bigdata.util.PropertiesConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：High Restful Api
 * @Author 01381119
 * @CreateDate: 2018/11/28 10:58
 */
public class RestHighLevelClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(RestHighLevelClientFactory.class);

    private String REST_HTTP_URL = PropertiesConfig.getValue("high.rest.http.url");// 多个ES搜索服务url以逗号分隔

    private static final int MAX_CONNECT_NUM = 2 * Runtime.getRuntime().availableProcessors() + 1;

    private static final String SCHEMA = "http";

    private static final int CONNECT_TIME_OUT = 1000;

    private static final int SOCKET_TIME_OUT = 30000;

    private static final int CONNECTION_REQUEST_TIME_OUT = 500;

    private static RestHighLevelClient restHighLevelClient;
    private static RestClient restClient;

    private List<String> httpHostList = new ArrayList<>();

    private static RestHighLevelClientFactory restClientFactory;

    static {
        restClientFactory = new RestHighLevelClientFactory();
        restClientFactory.init();
    }

    public static RestHighLevelClientFactory getInstance() {
        return restClientFactory;
    }

    public void init() {

        if (restHighLevelClient != null) {
            return;
        }

        if (StringUtils.isBlank(REST_HTTP_URL)) {
            logger.error("config配置中心未配置restful url[key=elastic.search.http.url]");
            return;
        }

        String[] hostAndPortArray = REST_HTTP_URL.split(",");
        HttpHost[] httpPosts = new HttpHost[hostAndPortArray.length];
        for (int i = 0; i < hostAndPortArray.length; i++) {
            String host = hostAndPortArray[i].split(":")[0];
            String port = hostAndPortArray[i].split(":")[1];
            httpPosts[i] = new HttpHost(host, Integer.parseInt(port), SCHEMA);
            httpHostList.add(hostAndPortArray[i]);
        }

        RestClientBuilder builder = RestClient.builder(httpPosts);
        setConnectTimeOutConfig(builder);
        setMutiConnectConfig(builder);
        System.out.println("REST_HTTP_URL: " + REST_HTTP_URL);
        logger.info("Elasticsearch Java High Level REST Client init start!");
        //6.x 以上
        restHighLevelClient = new RestHighLevelClient(builder);
        //5.x
//        restClient = builder.build();
//        restHighLevelClient = new RestHighLevelClient(restClient);

        logger.info("Elasticsearch Java High Level REST Client init Successfully!");
    }


    /**
     * 主要关于异步httpclient的连接延时配置
     */
    public void setConnectTimeOutConfig(RestClientBuilder builder) {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIME_OUT);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIME_OUT);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
            return requestConfigBuilder;
        });
    }

    /**
     * 主要关于异步httpclient的连接数配置
     */
    public void setMutiConnectConfig(RestClientBuilder builder) {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONNECT_NUM);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_NUM);
            return httpClientBuilder;
        });
    }

    public RestHighLevelClient getHighLevelClient() {

        if (restHighLevelClient == null) {
            logger.error("Elasticsearch Java High Level REST Client init error!");
            return null;
        }
        return restHighLevelClient;
    }

    public void close() {
        if (restHighLevelClient != null) {
            try {
                restClient.close();
            } catch (Exception e) {
                logger.error("close the rest high level client error:", e);
            }
        }
    }
}