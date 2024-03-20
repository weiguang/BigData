package com.okayjam.bigdata.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;


/**
 * com.okayjam.bigdata.rocketmq
 *
 * @author JamChen chen2621978@gmail.com
 * @date 2024/03/20 15:27
 **/


public class ProducerDemo {
	public static void main(String[] args) throws Exception {
		// 创建生产者
		DefaultMQProducer producer = new DefaultMQProducer("your_group_name");
		producer.setNamesrvAddr("9.135.90.195:9876");
		producer.start();

		// 创建消息
		Message msg = new Message("TopicTest", "your_tag", "jam-test".getBytes());

		// 发送消息
		SendResult sendResult = producer.send(msg);
		System.out.println("Send result: " + sendResult);

		// 关闭生产者
		producer.shutdown();
	}
}