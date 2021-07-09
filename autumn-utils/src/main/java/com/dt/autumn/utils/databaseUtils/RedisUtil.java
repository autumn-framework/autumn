package com.dt.autumn.utils.databaseUtils;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Reporter;
import redis.clients.jedis.*;

import java.util.*;

public class RedisUtil {

	private static RedisUtil redisUtil;
	private static Map<String, Jedis> redisConnectionMap = new HashMap<>();

	private RedisUtil() {
	}

	public static RedisUtil getInstance() {
		if(redisUtil==null){
			synchronized (RedisUtil.class){
				if(redisUtil==null){
					redisUtil = new RedisUtil();
				}
			}
		}
		return redisUtil;
	}

	private synchronized Jedis getConnection(String redisURI) {

		boolean isSentinelEnabled = false;

		if (redisConnectionMap.containsKey(redisURI)) {
			Jedis jedis = redisConnectionMap.get(redisURI);
			if (jedis.isConnected()) {
				return jedis;
			} else {
				redisConnectionMap.remove(redisURI);
			}
		}

		Reporter.log("<br>Redis URI Detected as : " + redisURI);
		String[] schemeSplit = redisURI.split("://");
		if (schemeSplit.length != 2) {
			throw new IllegalArgumentException(redisURI + " is not a valid Redis URI");
		}
		if (schemeSplit[0].equals("redis-sentinel")) {
			isSentinelEnabled = true;
		} else if (!schemeSplit[0].equals("rediss")) {
			throw new IllegalArgumentException(redisURI + " is not a valid Redis URI");
		}

		String hostName =schemeSplit[1].split("@")[1].split(":")[0] ;
		int port = Integer.parseInt(schemeSplit[1].split("@")[1].split(":")[1]) ;
		String password =schemeSplit[1].split("@")[0];


		Reporter.log("<br>Using is Sentinel Enabled as : " + isSentinelEnabled);
		String[] clusterSplit = schemeSplit[1].split("#");
		if (clusterSplit.length > 2) {
			throw new IllegalArgumentException(redisURI + " is not a valid Redis URI");
		}

		String clusterName = null;
		if (clusterSplit.length == 2) {
			clusterName = clusterSplit[1];
		}
		Reporter.log("<br>Using Redis Cluster name as : " + clusterName);
		String[] addresses = clusterSplit[0].split("/")[0].split(",");
		Jedis jedis;
		if (isSentinelEnabled) {
			Set<String> sentinels = new HashSet<String>(Arrays.asList(addresses));
			jedis = new JedisSentinelPool(clusterName, sentinels, getJedisPoolConfig(), password).getResource();
		} else {
			jedis = new JedisPool(getJedisPoolConfig(), hostName, port,Protocol.DEFAULT_TIMEOUT, password).getResource();
		}

		redisConnectionMap.put(redisURI, jedis);
		return jedis;
	}

	private JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(400);
		poolConfig.setMaxIdle(400);
		poolConfig.setMinIdle(10);
		poolConfig.setMaxWaitMillis(5000);
		poolConfig.setFairness(true);
		poolConfig.setBlockWhenExhausted(false);
		poolConfig.setTestOnCreate(true);
		return poolConfig;
	}

	private void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public void setRedisKey(String redisURI, String key, String value) {
		Jedis jedis = getConnection(redisURI);
		jedis.set(key, value);
	}

	public String getString(String redisURI, String key) {
		Jedis jedis = getConnection(redisURI);
		String value = jedis.get(key);
		return value != null ? String.valueOf(value) : null;
	}

	public String getHGet(String redisURI, String key, String field) {
		Jedis jedis = getConnection(redisURI);
		String value = jedis.hget(key,field);
		return value != null ? String.valueOf(value) : null;
	}

	public Map<String,String> getHGetAll(String redisURI, String key) {
		Jedis jedis = getConnection(redisURI);
		Map<String,String> value = jedis.hgetAll(key);
		return value != null ? value : null;
	}

	public Boolean keyExists(String redisURI, String key) {
		Jedis jedis = getConnection(redisURI);
		return jedis.exists(key);
	}

	public Boolean keyHExists(String redisURI, String key, String field) {
		Jedis jedis = getConnection(redisURI);
		return jedis.hexists(key, field);
	}

	public String getJsonString(String redisURI, String key) {
		ObjectMapper mapper = new ObjectMapper();
		String value = getString(redisURI, key);
		if (null == value) {
			return null;
		} else {
			try {
				return mapper.writeValueAsString(value);
			} catch (JsonProcessingException e) {
				Reporter.log(e.toString());
				return null;
			}
		}
	}

	public void flushAll(String redisURI) {
		Jedis connection = getConnection(redisURI);
		connection.flushAll();
	}

	public void delete(String redisURI, String... keys) {
		Jedis jedis = getConnection(redisURI);
		jedis.del(keys);
	}

	public void deleteKeyPattern(String redisURI, String pattern) {
		Jedis jedis = getConnection(redisURI);
		Set<String> keys = jedis.keys(pattern);
		for (String key : keys) {
			jedis.del(key);
		}
	}

	public void closeAllConnections() {
		Set<String> keys = redisConnectionMap.keySet();
		for (String key : keys) {
			Jedis connection = redisConnectionMap.get(key);
			if (!connection.isConnected()) {
				connection.close();
				connection.disconnect();
			}
			redisConnectionMap.remove(key);
		}
	}

	public Set<String> allPatternBasedRedisKey(String redisURI, String pattern) {
		Jedis jedis = getConnection(redisURI);
		Set<String> keys = jedis.keys(pattern);
		return keys;
	}

}
