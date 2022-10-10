package com.okayjam.bigdata.flink.kafka;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2022/10/10 14:21
 **/
public class MessageDeSerializationSchema implements KafkaDeserializationSchema<ConsumerRecord<String, String>> {

	@Override
	public boolean isEndOfStream(ConsumerRecord<String, String> stringStringConsumerRecord) {
		return false;
	}

	@Override
	public ConsumerRecord<String, String> deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
		//返回ConsumerRecord
		return new ConsumerRecord<>(
				consumerRecord.topic(),
				consumerRecord.partition(),
				consumerRecord.offset(),
				new String(consumerRecord.key()),
				new String(consumerRecord.value())
		);
	}

	@Override
	public TypeInformation<ConsumerRecord<String, String>> getProducedType() {
		return TypeInformation.of(new TypeHint<ConsumerRecord<String, String>>(){});
	}
}

