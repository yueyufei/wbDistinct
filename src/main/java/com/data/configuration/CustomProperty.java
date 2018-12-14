package com.golaxy.configuration;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

public class CustomProperty {
	// kafka Config
	public static final String kafkaBootstrapServers;
	public static final String kafkaApplicationId;
	public static final int kafkaCommitIntervalMs;
	public static final int kafkaThreadNumber;
	public static final String kafkaAutoOffsetReset;
	public static final String kafkaSecurityProtocol;
	public static final String kafkaSaslMechanism;
	public static final String kafkaSaslKerberosServiceName;
	public static final String kafkaJavaSecurityAuthLogin;
	public static final String kafkaProducerTopic;
	public static final Set<String> kafkaTopics = new HashSet<String>();
	// redis config
	public static final String redisAddress;
	public static final Integer redisPort;
	public static final Integer redisTimeOut;
	public static final Integer redisMaxTotal;
	public static final Integer redisMaxIdle;
	public static final Long redisMaxWait;
	public static final Boolean redisTestOnBorrow;
	public static final Boolean redisTestOnReturn;
	public static final Integer redisKeyExpireTime;
	private static String FILE_NAME = "conf/wbdistinct.properties";
	public static final Integer redisDataBase;
	static {
		Properties rb = new Properties();
		try {
			PropertyConfigurator.configure("conf/log4j.properties");
			rb.load(new FileInputStream(FILE_NAME));
			kafkaBootstrapServers = rb.getProperty("kafka.bootstrap.servers");
			kafkaApplicationId = rb.getProperty("kafka.application.id");
			kafkaCommitIntervalMs = Integer.parseInt(rb.getProperty("kafka.commit.interval.ms"));
			kafkaAutoOffsetReset = rb.getProperty("kafka.auto.offset.reset");
			kafkaSecurityProtocol = rb.getProperty("kafka.security.protocol");
			kafkaSaslMechanism = rb.getProperty("kafka.sasl.mechanism");
			kafkaSaslKerberosServiceName = rb.getProperty("kafka.sasl.kerberos.service.name");
			kafkaJavaSecurityAuthLogin = rb.getProperty("kafka.java.security.auth.login.config");
			kafkaThreadNumber = Integer.parseInt(rb.getProperty("kafka.number.stream.thread"));
			String[] topics = rb.getProperty("kafka.stream.topics").split(",");
			kafkaTopics.addAll(Arrays.asList(topics));
			kafkaProducerTopic = rb.getProperty("kafka.producer.topic");


			redisAddress = rb.getProperty("redis.address", "127.0.0.1");
			redisPort = Integer.valueOf(rb.getProperty("redis.port", "6379"));
			redisTimeOut = Integer.valueOf(rb.getProperty("redis.time.out", "60000"));
			redisMaxTotal = Integer.valueOf(rb.getProperty("redis.max.total", "20"));
			redisMaxIdle = Integer.valueOf(rb.getProperty("redis.max.idle", "5"));
			redisMaxWait = Long.valueOf(rb.getProperty("redis.max.wait.millis", "1000"));
			redisTestOnBorrow = Boolean.valueOf(rb.getProperty("redis.test.on.borrow", "true"));
			redisTestOnReturn = Boolean.valueOf(rb.getProperty("redis.test.on.return", "true"));
			redisKeyExpireTime = Integer.valueOf(rb.getProperty("redis.key.expire.time"));
			redisDataBase = Integer.valueOf(rb.getProperty("redis.dataBase"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {
		System.out.println(kafkaBootstrapServers);
		System.out.println(kafkaApplicationId);
		System.out.println(kafkaCommitIntervalMs);
		System.out.println(kafkaThreadNumber);
		System.out.println(kafkaAutoOffsetReset);
		System.out.println(kafkaSecurityProtocol);
		System.out.println(kafkaSaslMechanism);
		System.out.println(kafkaSaslKerberosServiceName);
		System.out.println(kafkaJavaSecurityAuthLogin);
		System.out.println(kafkaTopics.toString());
		System.out.println(redisAddress);
		System.out.println(redisPort);
		System.out.println(redisTimeOut);
		System.out.println(redisMaxTotal);
		System.out.println(redisMaxIdle);
		System.out.println(redisMaxWait);
		System.out.println(redisTestOnBorrow);
		System.out.println(redisTestOnReturn);
		System.out.println(redisKeyExpireTime);


	}

}
