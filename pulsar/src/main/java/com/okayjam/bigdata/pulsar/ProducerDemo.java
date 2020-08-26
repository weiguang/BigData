package com.okayjam.bigdata.pulsar;


import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;


/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/02/18 19:42
 **/
public class ProducerDemo {
    public static void main(String[] args) throws PulsarClientException {
        String serverUrl = "pulsar://xxx:6650";
        String topic = "my-topic";
        Producer<byte[]> producer = getProducer(serverUrl, topic);
        send1(producer);
        send2(producer);
        producer.close();

    }
    public static Producer getProducer(String serverUrl, String topic) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(serverUrl)
                .build();
        Producer producer = client.newProducer()
                .topic(topic)
                .create();
        return producer;
    }

    public  static void send1(Producer producer) throws PulsarClientException {
        producer.send("My message".getBytes());
    }

    public  static void send2(Producer producer) throws PulsarClientException {
        producer.newMessage()
                .key("my-message-key")
                .value("my-async-message".getBytes())
                .property("my-key", "my-value")
                .property("my-other-key", "my-other-value")
                .send();
    }
}
