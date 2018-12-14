package com.golaxy.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.golaxy.configuration.CustomProperty;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

	private JedisPool jedisPool;
	private int maxTotal = CustomProperty.redisMaxTotal;
	private int maxIdle = CustomProperty.redisMaxIdle;
	private long maxWait = CustomProperty.redisMaxWait;
	private boolean testOnBorrow = CustomProperty.redisTestOnBorrow;
	private boolean testOnReturn = CustomProperty.redisTestOnReturn;
	private Logger logger = Logger.getLogger(RedisPool.class);
	private static RedisPool redisPool = null;

	public static RedisPool getInstance() {
		synchronized (RedisPool.class) {
			if (redisPool == null) {
				return redisPool = new RedisPool();
			}
		}
		return redisPool;
	}

	public RedisPool(String host, int port, int timeout, Integer dataBase) {
		logger.info("init redis connect...");
		logger.info("redis connect host is " + host + "\t" + "port is " + port);
		try {
			logger.info("init redis pool config...");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMaxWaitMillis(maxWait);
			config.setTestOnBorrow(testOnBorrow);
			config.setTestOnReturn(testOnReturn);
			logger.info("redis max total " + config.getMaxTotal());
			logger.info("redis max idle " + config.getMaxIdle());
			logger.info("redis max wait millis " + config.getMaxWaitMillis());
			// jedisPool = new JedisPool(config, host, port, timeout);
			jedisPool = new JedisPool(config, host, port, timeout, null, dataBase);
			logger.info("Initialization is completed...");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public RedisPool() {
		this(CustomProperty.redisAddress, CustomProperty.redisPort, CustomProperty.redisTimeOut,
				CustomProperty.redisDataBase);
	}

	public RedisPool(String host) {
		this(host, CustomProperty.redisPort, CustomProperty.redisTimeOut, CustomProperty.redisDataBase);
	}

	public RedisPool(String host, int port) {
		this(host, port, CustomProperty.redisTimeOut, CustomProperty.redisDataBase);
	}


	public void close() {
		if (jedisPool != null && !jedisPool.isClosed()) {
			jedisPool.destroy();
		}
	}

	public boolean set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String res = jedis.set(key, value);
			return (res == null || !res.equalsIgnoreCase("OK") ? false : true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public boolean set(String key, String value, int expire_time) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String res = jedis.set(key, value, "NX", "EX", expire_time);
			return (res == null || !res.equalsIgnoreCase("OK") ? false : true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public boolean setex(String key, String value, int expire_time) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String res = jedis.setex(key, expire_time, value);
			return (res == null || !res.equalsIgnoreCase("OK") ? false : true);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Set<String> smembers(String key) {
		Set<String> members = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			members = jedis.smembers(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return members;
	}

	public Set<String> sunion(String keys) {
		Set<String> union = new HashSet<String>();

		String[] array_key = keys.split(";");
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			union = jedis.sunion(array_key[0], array_key[1], array_key[2], array_key[3], array_key[4], array_key[5],
					array_key[6], array_key[7], array_key[8], array_key[9], array_key[10], array_key[11], array_key[12],
					array_key[13], array_key[14], array_key[15], array_key[16], array_key[17], array_key[18],
					array_key[19], array_key[20], array_key[21], array_key[22], array_key[23], array_key[24],
					array_key[25], array_key[26], array_key[27]);

			// logger.info("union: " + union.toString());

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return union;
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String res = jedis.get(key);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public boolean expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			long ret = jedis.expire(key, seconds);
			// System.out.println("isConnected:" + jedis.isConnected());
			// System.out.println("expire return:" + ret);
			return ret == 1 ? true : false;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Long publish(String channel, String message) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long ret = jedis.publish(channel, message);
			return ret;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1l;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Long rpush(String key, String... values) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rpush(key, values);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1l;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public String lpop(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lpop(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public long hmset(String key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String reply = jedis.hmset(key, hash);
			return reply != null && reply.equalsIgnoreCase("ok") ? 0 : -1;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public List<String> hmget(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> reply = jedis.hmget(key, fields);
			return reply;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String reply = jedis.hget(key, field);
			if (reply == null || reply.equals("nil")) {
				return null;
			}
			return reply;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public boolean sadd(String key, String... member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.sadd(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return true;
	}

	public boolean exist(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

}
