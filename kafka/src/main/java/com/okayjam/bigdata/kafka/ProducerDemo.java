package com.okayjam.bigdata.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

/**
 * @author: Chen weiguang <weiguangchen@sf-express.com>
 * @create: 2019/02/18 19:42
 **/
public class ProducerDemo {
    public static void main(String[] args) {

        String topic = "test";
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.20.120:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        int count = 0;
        while (count++ < 100) {
            Scanner sn = new Scanner(System.in);
            String str = sn.nextLine();
            producer.send(new ProducerRecord<String, String>(topic,str, str));
        }
        producer.close();
    }

}
