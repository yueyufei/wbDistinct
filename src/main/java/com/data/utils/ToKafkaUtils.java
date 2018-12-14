package com.golaxy.utils;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import com.golaxy.configuration.CustomProperty;


public class ToKafkaUtils {
	public static Producer<String, String> producer;
	public static Logger logger = Logger.getLogger(ToKafkaUtils.class);
	
	static {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CustomProperty.kafkaBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,  "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  "org.apache.kafka.common.serialization.StringSerializer");
        props.put("security.protocol", CustomProperty.kafkaSecurityProtocol);
		props.put("sasl.mechanism", CustomProperty.kafkaSaslMechanism);
		props.put("sasl.kerberos.service.name", CustomProperty.kafkaSaslKerberosServiceName);
		System.setProperty("java.security.auth.login.config", CustomProperty.kafkaJavaSecurityAuthLogin);
		producer = new KafkaProducer<>(props);
	}
	
	
	public static void sendMsgs(final String topic, final String key, final String value){
		if (producer == null) {
			logger.error("Producer is not initialized, please initialize it first.");
			return;
		}
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);
		producer.send(record, new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
                     if(exception != null) {
                    	 exception.printStackTrace();
                         logger.error("发送失败, topic:"+topic+" key:"+key);
                         logger.error("错误信息: "+exception.getMessage());
                         return;
                     }
			}
		});
	}
}
