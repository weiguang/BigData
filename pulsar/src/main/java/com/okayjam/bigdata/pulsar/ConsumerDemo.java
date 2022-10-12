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
                .serviceUrl("pulsar://9.135.90.195:6650")
                .build();
//        consumerByListener(client);
        consumer(client);
    }


    public static void consumerByListener(PulsarClient client ) throws PulsarClientException {
        client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscriptionByListener")
                .messageListener( (consumer, msg) -> {
                    try {
                        System.out.println("Message received: " + new String(msg.getData()));
                        consumer.acknowledge(msg);
                    } catch (Exception e) {
                        consumer.negativeAcknowledge(msg);
                    }
                })
                .subscribe();
    }

    public static void consumer(PulsarClient client ) throws PulsarClientException {
        Consumer consumer = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscribe();

        while (true) {
            Message msg = consumer.receive();
            try {
                // Do something with the message
                System.out.printf("Message received: %s, key:%s \n", new String(msg.getData()), msg.getKey());
                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
                Thread.sleep(1000);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }

    }


}



