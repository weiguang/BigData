package com.okayjam.bigdata.kafka.stream;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2023/01/13 11:09
 * if us console output:
 * bin/kafka-console-consumer.sh --topic streams-wordcount-output --from-beginning --bootstrap-server localhost:9092
 * --property print.key=true  --value-deserializer org.apache.kafka.common.serialization.LongDeserializer
 **/
public class WordCountDemo {
	public static final String INPUT_TOPIC = "streams-plaintext-input";
	public static final String OUTPUT_TOPIC = "streams-wordcount-output";

	static Properties getStreamsConfig() {
		final Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

		// setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
		// Note: To re-run the demo, you need to use the offset reset tool:
		// https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Application+Reset+Tool
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	static void createWordCountStream(final StreamsBuilder builder) {
		final KStream<String, String> source = builder.stream(INPUT_TOPIC);

		final KTable<String, Long> counts = source
				.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
				.groupBy((key, value) -> value)
				.count();

		// need to override value serde to Long type
		counts.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

	}

	public static void main(final String[] args) {
		final Properties props = getStreamsConfig();

		final StreamsBuilder builder = new StreamsBuilder();
		createWordCountStream(builder);
		final Topology topology = builder.build();
		System.out.println(topology.describe());
		final KafkaStreams streams = new KafkaStreams(topology, props);
		final CountDownLatch latch = new CountDownLatch(1);

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
				latch.countDown();
			}
		});

		try {
			streams.start();
			latch.await();
		} catch (final Throwable e) {
			System.exit(1);
		}
		System.exit(0);
	}
}
