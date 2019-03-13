package com.okayjam.bigdata;

import com.okayjam.bigdata.elasticsearch.TransportClientFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/13 18:55
 **/
public class ClientTest {
    @Test
    public void getClient() {
        TransportClient client = TransportClientFactory.getInstance().getTransportClient();
    }
}