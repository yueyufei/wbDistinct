package com.golaxy.utils;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.log4j.Logger;

import com.golaxy.configuration.CustomProperty;
import com.google.gson.JsonObject;


public class KafkaStreamUtil {
	public static StreamsBuilder builder = new StreamsBuilder();
	public static Logger logger = Logger.getLogger(KafkaStreamUtil.class);

	private KafkaStreamUtil() {
	}

	public static Properties getStreamconfig() {
		Properties props = new Properties();

		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, CustomProperty.kafkaBootstrapServers);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, CustomProperty.kafkaCommitIntervalMs);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, CustomProperty.kafkaAutoOffsetReset);
		props.setProperty("security.protocol", CustomProperty.kafkaSecurityProtocol);
		props.setProperty("sasl.mechanism", CustomProperty.kafkaSaslMechanism);
		props.setProperty("sasl.kerberos.service.name", CustomProperty.kafkaSaslKerberosServiceName);
		System.setProperty("java.security.auth.login.config", CustomProperty.kafkaJavaSecurityAuthLogin);
		return props;
	}

	public static void kafkaStreamStart() {
		KStream<String, String> stream = builder.stream(CustomProperty.kafkaTopics);
		Properties props = KafkaStreamUtil.getStreamconfig();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, CustomProperty.kafkaApplicationId);
		stream.foreach(new ForeachAction<String, String>() {
			@Override
			public void apply(String key, String value) {
				JsonObject valueObj = JsonUtils.str2Obj(value);
				String line = valueObj.get("content").getAsString();
				String id = valueObj.get("_id").getAsString();
				line = Regex.regex(line);
				line = Regex.replaceAllemoji(line);
				line = Regex.replaceAllUrl(line);
				line = Regex.removePunctuation(line);
				line = Regex.replaceAllS(line);
				String bkdrHash = HashCode.BKDRHash(line) + "";
				RedisPool redisPool = RedisPool.getInstance();
				if (redisPool.exist(bkdrHash)) {
					String smid = redisPool.get(bkdrHash);
					if (!smid.equals(id)) {
						valueObj.addProperty("smid", smid);
						logger.info(
								"content is already exist the bkdrHash is .the similar id is " + smid);
					}
				} else {
					boolean isSuccess = redisPool.setex(bkdrHash, id, CustomProperty.redisKeyExpireTime);
					if (isSuccess) {
						logger.info("redis not contains id " + id + " and this id has been recorded");
					}
				}
				ToKafkaUtils.sendMsgs(CustomProperty.kafkaProducerTopic, UUID.randomUUID().toString(),
						JsonUtils.obj2Str(valueObj));
			}
		});
		Topology topo = builder.build();
		KafkaStreams streams = new KafkaStreams(topo, props);
		streams.start();

	}
}