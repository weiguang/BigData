package com.okayjam.bigdata.kafka;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/02/18 18:31
 **/
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ConsumerDemo {
    private static final Logger logger =  LoggerFactory.getLogger(ConsumerDemo.class);

    public static void main(String[] args) throws InterruptedException {

        String topicName = "mykafka1";
        String groupId = "test-consumer-group1";

        Properties props = new Properties();
        //必须指定
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topicName));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }

}

