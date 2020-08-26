package com.okayjam.bigdata.pulsar;


import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;

import java.util.Scanner;


/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/02/18 19:42
 **/
public class ProducerDemo {

    private static PulsarClient client = null;

    /**
     * 获取client
     * @param serverUrl
     * @return
     * @throws PulsarClientException
     */
    public static PulsarClient getClient(String serverUrl) throws PulsarClientException {
        // 如果已经初始化直接返回
        if (client == null) {
            // 加锁初始化
            synchronized(ProducerDemo.class) {
                // double check
                if (client == null) {
                    client = PulsarClient.builder()
                            .serviceUrl(serverUrl)
                            .build();
                }
            }
        }
        return client;
    }

    /**
     * 获取生产者
     * @param serverUrl
     * @param topic
     * @return
     * @throws PulsarClientException
     */
    public static Producer getProducer(String serverUrl, String topic) throws PulsarClientException {
        Producer producer = getClient(serverUrl).newProducer()
                .topic(topic)
                .create();
        return producer;
    }

    public static void main(String[] args) throws PulsarClientException {
        String serverUrl = "pulsar://9.135.90.195:6650";
        String topic = "my-topic";
        Producer<byte[]> producer = getProducer(serverUrl, topic);
        send1(producer);
        send2(producer);
        send3(producer);
        producer.close();
        client.close();
    }



    public  static void send1(Producer producer) throws PulsarClientException {
        producer.send("My message2".getBytes());
    }

    public  static void send2(Producer producer) throws PulsarClientException {
        producer.newMessage()
                .key("my-message-key")
                .value("my-async-message".getBytes())
                .property("my-key", "my-value")
                .property("my-other-key", "my-other-value")
                .send();
    }

    public  static void send3(Producer producer) throws PulsarClientException {
        while(true) {
            Scanner sn = new Scanner(System.in);
            String line = sn.nextLine();
            producer.send(line.getBytes());
            if (StringUtils.equals(line, "bye")) {
                break;
            }
        }
    }
}
