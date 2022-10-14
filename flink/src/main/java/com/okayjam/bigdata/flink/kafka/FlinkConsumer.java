package com.okayjam.bigdata.flink.kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2022/10/10 14:16
 **/
public class FlinkConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlinkConsumer.class);
	static String sourceKafka = "9.135.90.195:9092";
	static String SinkKafka = "9.135.90.195:9092";
	public static void main(String[] args) throws Exception{
		// 构建环境
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		//指定checkpoint的触发间隔
		env.enableCheckpointing(5000);
		// 默认的CheckpointingMode就是EXACTLY_ONCE，也可以指定为AT_LEAST_ONCE
		env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);


		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", sourceKafka);
		//设置消费者组
		properties.setProperty("group.id", "flink_consumer1");
		//消费的三种方式，默认是latest
		//earliest：各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
		//latest：各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
		//none： topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
		properties.setProperty("auto.offset.reset", "earliest");


		KafkaSource<String> source = KafkaSource.<String>builder().setProperties(properties)
//				.setBootstrapServers("9.135.90.195:9092")
				.setTopics("mykafka")
//				.setGroupId("my-group")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();


		DataStreamSource<String> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka-Source", TypeInformation.of(String.class));
//		stream.map(lines -> {
//			System.out.println(lines);
//			return lines;
//		});

		//sink
		KafkaSink<String> sink = KafkaSink.<String>builder()
				.setBootstrapServers(SinkKafka)
				.setRecordSerializer(KafkaRecordSerializationSchema.builder()
						.setTopic("mykafka1")
						.setValueSerializationSchema(new SimpleStringSchema())
						.build()
				)
				.build();



		 stream.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public void flatMap(String s, Collector<String> collector) throws Exception {
				LOGGER.info(s);
				System.out.println(s);
				collector.collect("new" + s);
			}
		}).sinkTo(sink);


		env.execute("consumer start");
	}


}
