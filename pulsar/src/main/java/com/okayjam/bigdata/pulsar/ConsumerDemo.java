package com.okayjam.bigdata.pulsar;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/02/18 18:31
 **/


import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConsumerDemo {
    private static final Logger logger =  LoggerFactory.getLogger(ConsumerDemo.class);

    public static void main(String[] args) throws  PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://xxx:6650")
                .build();

        Consumer consumer = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscribe();

        while (true) {
            // Wait for a message
            Message msg = consumer.receive();
            try {
                // Do something with the message
                System.out.printf("Message received: %s, key:%s ", new String(msg.getData()), msg.getKey());
                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }

    }


}

