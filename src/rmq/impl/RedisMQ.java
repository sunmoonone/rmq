package rmq.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rmq.mq.ConsumerQueue;
import rmq.proto.JedisPoolManager;
import rmq.util.ObjectSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisMQ implements ConsumerQueue {
	Logger logger = Logger.getLogger(RedisMQ.class);

	private JedisPoolManager poolManager;

	public RedisMQ() {
		this.poolManager = new JedisPoolManager();
	}
	
	public RedisMQ(JedisPoolManager pm){
		this.poolManager=pm;
	}

	public boolean exists(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.exists(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return false;
	}

	public void put(String key, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.set(key, value);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public void put(String key, Integer value) {
		JedisPool pool = null;
		Jedis jedis = null;
		String v = String.valueOf(value);
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.set(key, v);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public void put(String key, String value, int seconds) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.setex(key, seconds, value);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public Long incr(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.incr(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public void put(String key, Map<String, String> map) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.hmset(key, map);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public String getString(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.get(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return null;
	}

	public Map<String, String> getMap(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.hgetAll(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}

		}
		return null;
	}

	public void expire(String key, int seconds) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.expire(key, seconds);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}

		}
	}

	public byte[] get(byte[] key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.get(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}

	}

	public String set(byte[] key, byte[] value, long miliseconds) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			Long l = miliseconds / 1000l;

			return jedis.setex(key, l.intValue(), value);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}

	}

	public Long del(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		int rt = 5;
		try {
			while (rt-- > 0) {
				try {
					pool = poolManager.getPool();
					jedis = pool.getResource();
					return jedis.del(key);
				} catch (Exception ex) {
					logger.error("jedis error", ex);
				}
			}
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return 0l;

	}

	public void lpush(String key, byte[] values) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.lpush(key.getBytes(), values);

		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}

	}

	public Long lpush(String key, Object obj) {

		JedisPool pool = null;
		Jedis jedis = null;
		int rt = 5;
		try {
			while (rt-- > 0) {
				try {
					pool = poolManager.getPool();
					jedis = pool.getResource();

					return jedis
							.lpush(key.getBytes(), ObjectSerializer.se(obj));

				} catch (Exception ex) {
					logger.error("jedis error", ex);
				}
			}
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return 0l;

	}

	/**
	 * 
	 * @param key
	 * @param timeout
	 *            in seconds
	 * @return
	 */
	public Object brpop(String key, int timeout) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			List<byte[]> list = jedis.brpop(timeout, key.getBytes());
			if (list == null || list.size() == 0)
				return null;
			return ObjectSerializer.de(list.get(0));

		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public Object brpoplpush(String source, String destination, int timeout) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			byte[] bs = jedis.brpoplpush(source.getBytes(),
					destination.getBytes(), timeout);
			return ObjectSerializer.de(bs);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public Long lrem(String key, Long count, Object obj) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();

			return jedis.lrem(key.getBytes(), count, ObjectSerializer.se(obj));

		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return 0l;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public Long lremFromTail(String key, Object obj) {
		JedisPool pool = null;
		Jedis jedis = null;
		int rt = 5;
		try {
			while (rt-- > 0) {
				try {
					pool = poolManager.getPool();
					jedis = pool.getResource();

					return jedis.lrem(key.getBytes(), -1,
							ObjectSerializer.se(obj));

				} catch (Exception ex) {
					logger.error("jedis error", ex);
					return 0l;
				}
			}
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return 0l;
	}

	public Long hdel(String key, String... fields) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.hdel(key, fields);

		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return 0l;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public Object getObject(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return ObjectSerializer.de(jedis.get(key.getBytes()));
		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public String setObject(String key, Object obj) {
		JedisPool pool = null;
		Jedis jedis = null;
		int rt = 5;
		try {
			while (rt-- > 0) {
				try {
					pool = poolManager.getPool();
					jedis = pool.getResource();
					return jedis.set(key.getBytes(), ObjectSerializer.se(obj));
				} catch (Exception ex) {
					logger.error("jedis error", ex);
				}
			}
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return "FAIL";
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> lrange(String key, int start, int stop) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();

			List<byte[]> ret = jedis.lrange(key.getBytes(), start, stop);

			if (ret == null || ret.size() == 0)
				return null;
			List<T> ls = new ArrayList<T>();

			for (byte[] bs : ret) {
				ls.add((T) ObjectSerializer.de(bs));
			}
			return ls;

		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public Long llen(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.llen(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
			return null;
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	@Override
	public String get(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			return jedis.get(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return null;
	}

	@Override
	public Long hset(String key, String field, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		int rt=5;
		try{
		while(rt-- > 0){
			try {
				pool = poolManager.getPool();
				jedis = pool.getResource();
				return jedis.hset(key, field, value);
			} catch (Exception ex) {
				logger.error("jedis error", ex);
			}
		}
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return 0l;
	}

	@Override
	public String hget(String key, String field) {
		JedisPool pool = null;
		Jedis jedis = null;
		int rt=5;
		try{
		while(rt-- > 0){
			try {
				pool = poolManager.getPool();
				jedis = pool.getResource();
				return jedis.hget(key, field);
			} catch (Exception ex) {
				logger.error("jedis error", ex);
			}
		}
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
		return null;
	}

	@Override
	public void lempty(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = poolManager.getPool();
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception ex) {
			logger.error("jedis error", ex);
		} finally {
			if (jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}
	
	public String info(String section) {
		JedisPool pool=null;
		Jedis jedis = null;
		try{
			pool = poolManager.getPool();		
			jedis = pool.getResource();
			return jedis.info(section);		
		}catch(Exception ex){
			logger.error("jedis error", ex);
			return null;
		}finally{
			if(jedis!=null){
				pool.returnResource(jedis);
			}
		}
	}
	
	public String info() {
		JedisPool pool=null;
		Jedis jedis = null;
		try{
			pool = poolManager.getPool();		
			jedis = pool.getResource();
			return jedis.info();		
		}catch(Exception ex){
			logger.error("jedis error", ex);
			return null;
		}finally{
			if(jedis!=null){
				pool.returnResource(jedis);
			}
		}
	}

}
