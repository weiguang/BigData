package com.okayjam.bigdata.rocketmq;




import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * com.okayjam.bigdata
 *
 * @author JamChen chen2621978@gmail.com
 * @date 2024/03/20 15:29
 **/
public class ConsumerDemo {
	public static void main(String[] args) throws Exception {
		// 创建消费者
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("your_group_name");
		consumer.setNamesrvAddr("9.135.90.195:9876");
		consumer.subscribe("TopicTest", "*");

		// 设置消息监听器
		consumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
			for (MessageExt msg : msgs) {
				System.out.println("Received message: " + new String(msg.getBody()));
			}
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		});

		// 启动消费者
		consumer.start();
	}
}